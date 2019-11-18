// pages/test/test.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    firstFalg: false, //是否第一次来到此页面
    quesNaireId: 0,   //所点击问卷的id
    quesNaire: {},    //所点击问卷的所有信息
    questionNaires: [],   //登录用户所拥有的所有问卷

  },

  //从后台读取问卷
  readQuestionnaire:function(){
    var that = this
    wx.request({
      url: app.globalData.href+'readQuestionnaire',
      header: app.globalData.header,
      success:function(res){
        console.log(res.data)
        that.setData({
          questionNaires: res.data.quesNaires
        })
      }
    })
  },
  //跳转到新建问卷页面
  tonewQues: function (event) {
    wx.navigateTo({
      url: '../newQues/newQues',
    })
  },
  //点击问卷函数，弹出操作框
  clickQuesNaire: function(event){
    var that= this
    var id = event.currentTarget.dataset.id  //获得点击问卷的id并设置
    this.setData({
      quesNaireId: id,
    })
    var naires = that.data.questionNaires
    for (var item in naires) {
      if (naires[item].id == that.data.quesNaireId) {
        that.setData({
          quesNaire: naires[item]
        })
      }
    }
    wx.showActionSheet({
      itemList: ['预览','调查结果','添加问题','编辑问卷','删除问卷'],
      success: function(res){
        if(res.tapIndex == 0){
          //选择预览问卷时
          that.toPreview()
        }else if(res.tapIndex == 1){
          //选择调查结果的时候
          that.toResult()
        }else if(res.tapIndex == 2){
          //当选择添加问题时
          that.toaddQuestion()
        } else if (res.tapIndex == 3){
          //当选择编辑问卷时
          that.editQuesNaire()
        } else if (res.tapIndex == 4){
          //当选择删除问卷时
          that.delQuesNaire()
        }
      },
      fail: function(res){
        console.log("fail:" + res.tapIndex)
      }
    })
  },
  //当选择添加问题时执行的函数
  toaddQuestion: function () {
    //var id = event.currentTarget.dataset.id
    var that = this
    wx.navigateTo({
      url: '../addQuestion/addQuestion?quesNaire=' + JSON.stringify(that.data.quesNaire),
    })
  },
  //编辑问卷
  editQuesNaire: function(){
    var that = this
    wx.navigateTo({
      url: '../newQues/newQues?quesNaire=' + JSON.stringify(that.data.quesNaire),
    })
  },
  //删除问卷
  delQuesNaire:function(){
    var that = this
    console.log("delete::" + that.data.quesNaireId)
    wx.request({
      url: app.globalData.href+'delQuestionNaire',
      data: {
        quesNaireId: that.data.quesNaireId
      },
      method: 'POST',
      header: app.globalData.header,
      success: function(res){
        that.readQuestionnaire()
      }
    })
  },
  toPreview:function(){
    var that = this
    wx.navigateTo({
      url: '../preview/preview?quesNaire=' + JSON.stringify(that.data.quesNaire),
    })
  },
  toResult:function(){
    var that = this
    wx.navigateTo({
      url: '../result/result?quesNaire=' + JSON.stringify(that.data.quesNaire),
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("我是来自于转发")
    console.log(app.globalData.quesNaire)
    var that = this
    wx.request({
      url: app.globalData.href+'getInfo',
      data: {
        code: app.globalData.code,
        userInfo: app.globalData.userInfo,
        openFlag: 'getOpenid',
      },
      method: 'POST',
      header: app.globalData.header,
      success: function (res) {
        console.log(res.data)
        app.globalData.header.Cookie = 'JSESSIONID=' + res.data.sessionid
        that.readQuestionnaire()
        that.setData({
          firstFalg: true
        })
        if(app.globalData.quesNaire!=null){//当全局变量中的quesNaire不为空时
          //用户是通过转发页面跳转到登录页面登录进来的，直接跳转到答题界面
          wx.redirectTo({
            url: '../preview/preview?quesNaire=' + JSON.stringify(app.globalData.quesNaire),
            success: function(){//将全局变量中的queNaire置为null
              app.globalData.quesNaire=null
            }
          })
        }
      }
    })
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    if(this.data.firstFalg){
      this.readQuestionnaire()
    }
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  
  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  
  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  
  }
})