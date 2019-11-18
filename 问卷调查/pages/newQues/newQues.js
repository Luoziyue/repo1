// pages/newQues/newQues.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    quesNaire: {}
  },
  addQuestionNaire:function(data){
    var qnName = data.detail.value.qnName
    var explains = data.detail.value.explains
    var quesNaireId = this.data.quesNaire.id
    if(qnName){
      wx.request({
        url: app.globalData.href+'addQuestionNaire',
        data: {
          'qnName': qnName,
          'explains': explains,
          'quesNaireId': quesNaireId
        },
        method: "POST",
        header: app.globalData.header,
        success: function (res) {
          wx.switchTab({
            url: '../index/index',
          });
        }
      })
    }else{
      console.log("nonononnonono")
      wx.showModal({
        title: '请输入问卷名',
        content: '',
      })
    }
    
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    
    if (options.quesNaire){
      console.log(options.quesNaire)
      this.setData({
        quesNaire: JSON.parse(options.quesNaire)
      })
    }
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