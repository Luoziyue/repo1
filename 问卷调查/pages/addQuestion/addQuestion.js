// pages/addQuestion/addQuestion.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    quesNaire: {},
    questions: [],
    frist_flag: false,
  },
  //从后台读取问题列表
  readQuestions: function(){
    var that = this
    wx.request({
      url: app.globalData.href+'readQuestions',
      header: app.globalData.header,
      data: {
        quesNaireId: that.data.quesNaire.id
      },
      method: 'POST',
      success: function(res){
        console.log(res.data)
        that.setData({
          questions: res.data.questions,
        })
      }
    })
  },
  //跳转到添加题目页面
  toChooseQues:function(){
    var that = this
    wx.navigateTo({
      url: '../chooseQues/chooseQues?quesNaireId=' + that.data.quesNaire.id +'&question=""',
      success: function(res) {},
      fail: function(res) {},
      complete: function(res) {},
    })
  },

  clickQuestion: function(event){
    var that = this
    var questionid = event.currentTarget.dataset.questionid
    console.log(questionid)
    var questions = that.data.questions
    var question = {}
    for(var item in questions){
      if (questions[item].tid==questionid){
        question = questions[item]
      }
    }
    console.log(question)
    wx.showActionSheet({
      itemList: ['编辑问题', '删除问题'],
      success: function(res){
        if(res.tapIndex==0){
          that.editQuestion(question)
        }else if(res.tapIndex==1){
          that.delQuestion(question)
        }
      }
    })
  },

  editQuestion: function(data){
    var that = this
    wx.navigateTo({
      url: '../chooseQues/chooseQues?quesNaireId=' + that.data.quesNaire.id+'&question='+JSON.stringify(data),
    })
  },
  delQuestion:function(data){
    wx.request({
      url: app.globalData.href +'delQuestion',
      data: {
        tid: data.tid
      },
      method: 'POST',
      header: app.globalData.header
    })
    this.readQuestions()
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log("问卷" + options.quesNaire)
    this.setData({
      quesNaire: JSON.parse(options.quesNaire),
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
    if(this.data.frist_flag){
      this.readQuestions()
    }else{
      this.setData({
        frist_flag: true
      })
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