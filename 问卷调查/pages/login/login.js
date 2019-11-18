//index.js
//获取应用实例
const app = getApp()

Page({
  data: {
    motto: '微信登录',
    choose: false,
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    quesNaire: {}
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  onLoad: function (options) {
    //由于用户未登录而直接出现在答题页面，会跳转到登录页面，此时quesNaire不为null
    if (options.quesNaire!=null){
      //因为wx.navigateTo跳转不能携带参数，所以把数据放到全局变量中
      app.globalData.quesNaire=JSON.parse(options.quesNaire)
    }
    
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse){
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
        
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
          
        }
      })
    }
  },
  getUserInfo: function(e) {
    app.globalData.userInfo = e.detail.userInfo
    if(app.globalData.userInfo){
      this.setData({
        userInfo: e.detail.userInfo,
        hasUserInfo: true
      })
      wx.switchTab({
        url: '../index/index',
      })
      
      
    }
  },
  WeChatLogin: function(){
    
    //  wx.request({
    //     url: 'http://localhost:8080/LJDD/wxxcx',
    //     data: '',
    //     method: 'GET',
    //     header: {
    //        'content-type': 'application/json'
    //     },
    //     success: function(res){
    //        console.log(JSON.stringify(res)),
    //        wx.showModal({
    //           title: "dsad",
    //           content: '',
    //        })
    //     },
    //     fail: function(){
           
    //     }
    //  })
  },
  chooses:function(event){
    var that = this
    that.setData({
      choose: true
    })
  },
  hidden:function(){
    var that = this
    that.setData({
      choose: false
    })
  }
})
