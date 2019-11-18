// pages/preview/preview.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    quesNaire: {},
    questions: []
  },

  //从后台读取问题列表
  readQuestions: function () {
    var that = this
    wx.request({
      url: app.globalData.href + 'readQuestions',
      header: app.globalData.header,
      data: {
        quesNaireId: that.data.quesNaire.id
      },
      method: 'POST',
      success: function (res) {
        console.log(res.data)
        console.log(res.data.loginflag)
        //后台判断session中是否有user来判断用户是否登录
        //用户未登录，携带数据前往login页面
        if(res.data.loginflag!=null){
          wx.navigateTo({
            url: '../login/login?quesNaire='+JSON.stringify(that.data.quesNaire),
          })
        }else{//用户已登录
          that.setData({
            questions: res.data.questions,
          })
          console.log(that.data.questions)
        }
      }
    })
  },
  quesNaireSubmit: function(data){
    var that = this
    var temp = data.detail.value  //拿到提交的数据
    var i=0   //设置循环标志
    var quesData = []
    var questions = that.data.questions   //拿到所有题目
    for(i=0;i<questions.length;i++ ){   //遍历每道题目
      if(questions[i].must_do){   //如果题目为必答，判断是否为空
        if(temp[questions[i].tid]==""){  
          wx.showModal({    //提示用户还有必答题未答
            title: '您还有必答题未答',
            content: '',
          })
          break
        }
      }
      var obj = {}
      obj.tid = questions[i].tid
      obj.type = questions[i].type
      obj.value = temp[questions[i].tid]
      quesData.push(obj)
    }
    if(i==questions.length){  //如果有必答题未答，上面的循环会直接结束，i不会等于length
      console.log("所有必答题都答完，提交到服务器")
      console.log(quesData)
      wx.request({
        url: app.globalData.href +'quesNaireSubmit',
        data: {
          'quesData': quesData
        },
        method: 'POST',
        header: app.globalData.header,
        success: function(res){
        }
      })
    }
    console.log(data.detail.value)

  },
  onShareAppMessage:function(res){
    var that = this
    return{
      title: '转发',
      path: '/pages/preview/preview?quesNaire='+JSON.stringify(that.data.quesNaire),
      success: function(res){}
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("code:" + app.globalData.code)
    console.log("userInfo:" + app.globalData.userInfo)
    console.log(options)
    var quesTemp = decodeURIComponent(options.quesNaire)
    this.setData({
      quesNaire: JSON.parse(quesTemp)
    })
    
    this.readQuestions()
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