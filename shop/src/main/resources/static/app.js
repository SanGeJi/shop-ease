const { createApp } = Vue
const API = '/api'
function getToken(){ return localStorage.getItem('shop_token') }
function setToken(t){ t ? localStorage.setItem('shop_token',t) : localStorage.removeItem('shop_token') }

createApp({
  data(){
    return {
      user:null, route:'/login', isLogin:true,
      authForm:{username:'',password:''}, authError:'',
      products:[], categories:[], filter:{category:null,q:''},
      curProduct:null, curSkus:[], curSpecs:[], curSpecOptions:{}, curSkuValues:[], selectedOpts:{}, curSku:null, qty:1, reviews:[],
      cart:[], cartCount:0, cartTotal:0,
      orderForm:{receiver:'',phone:'',address:'',coupon_id:null},
      myCoupons:[], coupons:[], orders:[],
      addresses:[], showAddrForm:false, addrForm:{}, editAddrId:null, selectedAddrId:null,
      dash:{}, salesChart:null,
      adminProducts:[], adminOrders:[], adminCoupons:[], users:[],
      prodForm:{name:null}, catForm:{name:'',icon:''}, couponForm:{code:'',name:'',type:'fixed',value:0,minSpend:0,total:0},
      editingProd:null,
      pendingImage:null
    }
  },
  async mounted(){
    window.addEventListener('hashchange',()=>this.onHash())
    const tok=getToken()
    if(tok){
      try{ const r=await this.api('/auth/me'); this.user=r.data.user; this.onHash() }
      catch(e){ setToken(null); this.go('/login') }
    } else this.go('/login')
  },
  methods:{
    go(p){ location.hash=p },
    onHash(){
      this.route=location.hash.replace('#','')||'/login'
      if(this.route==='') this.route='/login'
      if(this.user&&this.route==='/login') this.route=this.user.role==='admin'?'/admin':'/'
      if(this.user){
        if(this.route==='/'&&!this.products.length) this.loadProducts()
        if(this.route==='/cart') this.loadCart()
        if(this.route==='/orders') this.loadOrders()
        if(this.route==='/coupons') this.loadCoupons()
        if(this.route==='/checkout'){ this.loadMyCoupons(); this.loadAddresses() }
        if(this.route==='/profile') this.loadAddresses()
        if(this.route==='/admin') this.loadDashboard()
        if(this.route==='/admin/products') this.loadAdminProducts()
        if(this.route==='/admin/categories') this.loadCategories()
        if(this.route==='/admin/orders') this.loadAdminOrders()
        if(this.route==='/admin/coupons') this.loadAdminCoupons()
        if(this.route==='/admin/users') this.loadUsers()
        if(this.route.startsWith('/product/')) this.loadProduct(this.route.split('/')[2])
      }
      if(!this.categories.length) this.loadCategories()
    },
    async api(path,opts={}){
      const tok=getToken()
      const isForm = opts.body instanceof FormData
      const headers = {...(isForm?{}:{'Content-Type':'application/json'}),
        ...(tok?{Authorization:'Bearer '+tok}:{}),
        ...(opts.headers||{})}
      const res=await fetch(API+path,{...opts,headers})
      const data=await res.json().catch(()=>({}))
      if(!res.ok) throw new Error(data.msg||'失败')
      return data
    },
    async submitAuth(){
      this.authError=''
      try{ const r=await this.api('/auth/'+(this.isLogin?'login':'register'),{method:'POST',body:JSON.stringify(this.authForm)})
        setToken(r.data.token); this.user=r.data.user
        this.go(r.data.user.role==='admin'?'/admin':'/')
        if(this.user.role==='customer'){ this.loadProducts() }
      } catch(e){ this.authError=e.message }
    },
    async logout(){ try{await this.api('/auth/logout',{method:'POST'})}catch(e){} setToken(null); this.user=null; this.go('/login') },

    async loadProducts(){
      const q=new URLSearchParams()
      if(this.filter.category) q.set('category',this.filter.category)
      if(this.filter.q) q.set('q',this.filter.q)
      const r=await this.api('/products?'+q.toString()); this.products=r.data.rows
    },
    async loadCategories(){ const r=await this.api('/categories'); this.categories=r.data.rows },
    async loadProduct(id){
      const r=await this.api('/products/'+id); const d=r.data
      this.curProduct=d.product; this.curSkus=d.skus; this.curSpecs=d.specs; this.curSpecOptions=d.specOptions; this.curSkuValues=d.skuValues
      this.selectedOpts={}; this.qty=1; this.curSku=null
      // 默认选第一个选项
      d.specs.forEach(sp=>{ if(d.specOptions[sp.id]&&d.specOptions[sp.id].length) this.selectedOpts[sp.id]=d.specOptions[sp.id][0].id })
      this.matchSku()
      const rv=await this.api('/products/'+id+'/reviews'); this.reviews=rv.data.rows
    },
    selectOpt(specId,optId){ this.selectedOpts[specId]=optId; this.matchSku() },
    matchSku(){
      // 找出选中的 option id 集合
      const sel=Object.values(this.selectedOpts)
      if(!sel.length||sel.length<this.curSpecs.length){ this.curSku=null; return }
      this.curSku=this.curSkus.find(sk=>{
        const sv=this.curSkuValues.filter(v=>v.skuId===sk.id)
        return sel.every(oId=>sv.some(v=>v.optionId===oId))
      })||null
    },
    minPrice(pid){ const sk=this.curSkus.filter(s=>s.productId===pid); return sk.length?Math.min(...sk.map(s=>s.price)):0 },
    async addToCart(){
      if(!this.curSku){ alert('请选择规格'); return }
      try{ await this.api('/cart',{method:'POST',body:JSON.stringify({sku_id:this.curSku.id,quantity:this.qty})}); alert('已加入购物车') }catch(e){ alert(e.message) }
    },
    async buyNow(){
      if(!this.curSku){ alert('请选择规格'); return }
      try{ await this.api('/cart',{method:'POST',body:JSON.stringify({sku_id:this.curSku.id,quantity:this.qty})}); this.go('/checkout'); this.loadCart() }catch(e){ alert(e.message) }
    },

    async loadCart(){ const r=await this.api('/cart'); this.cart=r.data.items; this.cartTotal=r.data.total; this.cartCount=this.cart.length },
    async updateCart(c){ await this.api('/cart/'+c.id,{method:'PUT',body:JSON.stringify({quantity:c.quantity})}); this.loadCart() },
    async delCart(id){ await this.api('/cart/'+id,{method:'DELETE'}); this.loadCart() },

    async loadMyCoupons(){ const r=await this.api('/my/coupons'); this.myCoupons=r.data.rows },
    async loadAddresses(){ const r=await this.api('/addresses'); this.addresses=r.data.rows; if(!this.selectedAddrId&&this.addresses.length) this.selectedAddrId=this.addresses[0].id },
    async saveAddress(){
      try{
        if(this.editAddrId){ await this.api('/addresses/'+this.editAddrId,{method:'PUT',body:JSON.stringify(this.addrForm)}) }
        else{ await this.api('/addresses',{method:'POST',body:JSON.stringify(this.addrForm)}) }
        this.showAddrForm=false; this.loadAddresses()
      }catch(e){ alert(e.message) }
    },
    async delAddress(id){ await this.api('/addresses/'+id,{method:'DELETE'}); this.loadAddresses() },
    async placeOrder(){
      const addr=this.addresses.find(a=>a.id===this.selectedAddrId)
      if(!addr){ alert('请选择收货地址'); return }
      this.orderForm.receiver=addr.receiver; this.orderForm.phone=addr.phone; this.orderForm.address=addr.address
      try{ await this.api('/orders',{method:'POST',body:JSON.stringify(this.orderForm)}); alert('下单成功!'); this.go('/orders') }catch(e){ alert(e.message) }
    },
    async loadOrders(){ const r=await this.api('/orders'); this.orders=r.data.rows },
    async payOrder(id){ try{ await this.api('/orders/'+id+'/pay',{method:'POST'}); alert('支付成功!'); this.loadOrders() }catch(e){ alert(e.message) } },

    async loadCoupons(){ const r1=await this.api('/coupons'); this.coupons=r1.data.rows; const r2=await this.api('/my/coupons'); this.myCoupons=r2.data.rows },
    async claimCoupon(id){ try{ await this.api('/coupons/'+id+'/claim',{method:'POST'}); alert('领取成功'); this.loadCoupons() }catch(e){ alert(e.message) } },

    async loadDashboard(){ const r=await this.api('/admin/dashboard'); this.dash=r.data; this.$nextTick(()=>this.renderChart()) },
    renderChart(){ if(this.salesChart) this.salesChart.destroy()
      // 简单:用订单数据做趋势(简化版)
      if(this.$refs.salesChart){ this.salesChart=new Chart(this.$refs.salesChart,{type:'line',data:{labels:['近7天'],datasets:[{label:'销售额',data:[this.dash.sales||0],borderColor:'#6366f1',fill:true}]},options:{responsive:true,plugins:{legend:{position:'bottom'}}}}) } },

    async loadAdminProducts(){ const r=await this.api('/admin/products'); this.adminProducts=r.data.rows },
    editProd(p){ this.editingProd=p.id; this.prodForm={name:p.name,description:p.description,categoryId:p.categoryId,imageUrl:p.imageUrl,status:p.status} },
    async saveProduct(){
      try{
        let id=this.editingProd
        if(!id){ const r=await this.api('/admin/products',{method:'POST',body:JSON.stringify(this.prodForm)}); id=r.data.id }
        else{ await this.api('/admin/products/'+id,{method:'PUT',body:JSON.stringify(this.prodForm)}) }
        if(this.pendingImage){
          const fd=new FormData(); fd.append('file',this.pendingImage)
          await this.api('/admin/products/'+id+'/image',{method:'POST',body:fd,headers:{}})
          this.pendingImage=null
        }
        this.prodForm={name:null}; this.editingProd=null; this.loadAdminProducts()
      }catch(e){ alert(e.message) }
    },
    async delProd(id){ if(!confirm('删除?'))return; await this.api('/admin/products/'+id,{method:'DELETE'}); this.loadAdminProducts() },
    onSelectImage(e){ this.pendingImage=e.target.files[0] },

    async saveCat(){ try{ await this.api('/admin/categories',{method:'POST',body:JSON.stringify(this.catForm)}); this.catForm={name:'',icon:''}; this.loadCategories() }catch(e){ alert(e.message) } },
    async delCat(id){ await this.api('/admin/categories/'+id,{method:'DELETE'}); this.loadCategories() },

    async loadAdminOrders(){ const r=await this.api('/admin/orders'); this.adminOrders=r.data.rows },
    async updateOrderStatus(id,s){ await this.api('/admin/orders/'+id,{method:'PATCH',body:JSON.stringify({status:s})}) },

    async loadAdminCoupons(){ const r=await this.api('/admin/coupons'); this.adminCoupons=r.data.rows },
    async saveCoupon(){ try{ await this.api('/admin/coupons',{method:'POST',body:JSON.stringify(this.couponForm)}); this.couponForm={code:'',name:'',type:'fixed',value:0,minSpend:0,total:0}; this.loadAdminCoupons() }catch(e){ alert(e.message) } },
    async delCoupon(id){ await this.api('/admin/coupons/'+id,{method:'DELETE'}); this.loadAdminCoupons() },

    async loadUsers(){ const r=await this.api('/admin/users'); this.users=r.data.rows },

    money(c){ return ((c||0)/100).toFixed(2) },
    emoji(url){ if(!url) return '📦'; if(url.startsWith('emoji:')) return url.slice(6); if(url.startsWith('/uploads/')) return url; return '📦' },
    isImageUrl(url){ return url && url.startsWith('/uploads/') },
    statusText(s){ return {pending:'待付款',paid:'已付款',shipped:'已发货',done:'已完成',cancelled:'已取消'}[s]||s },
    couponDesc(c){ return c.type==='fixed'?'满'+(c.minSpend/100)+'减'+(c.value/100):'满'+(c.minSpend/100)+'打'+(c.value/10)+'折' }
  }
}).mount('#app')
