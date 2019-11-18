// pages/result/result.js
var app = getApp()
let Charts = require('../../utils/wxcharts.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    quesNaire: {},
    questions: [],
    sortflag: 1  //1代表当前选项排序升序，2代表降序，需要用户手动选择，第一次默认降序
  },

  //从后台读取问题数据
  readQuestions: function() {
    var that = this
    wx.request({
      url: app.globalData.href + 'readQuestions',
      header: app.globalData.header,
      data: {
        quesNaireId: that.data.quesNaire.id
      },
      method: 'POST',
      success: function(res) {
        console.log(res.data)
        that.setData({
          questions: res.data.questions,
        })
        var questions = that.data.questions
        console.log(questions)
        that.compute_proportion()
        //that.drawPie()
      }
    })
  },
  //利用wxcharts插件绘制饼图
  drawPies: function(){
    var that = this
    var quess = that.data.questions
    for (var i = 0; i < quess.length; i++) {
      if(quess[i].type==3){
        continue
      }
      var pieseriae = []
      var options = quess[i].options
      for(var j=0;j<options.length;j++){
        var temp = {}
        temp.name = options[j].optionname
        temp.data = options[j].answers
        //temp.format = function(){return 0}
        pieseriae.push(temp)
      }
      new Charts({
        canvasId: quess[i].tid,
        type: 'pie',
        series: pieseriae,
        width: 340,
        height: 270,
        dataLabel: true,
      })
    }
  },
  drawPie: function (index) {
    var that = this
    var quess = that.data.questions
    if (quess[index].type == 3) {
      return
    }
    console.log('sd')
    var pieseriae = []
    var options = quess[index].options
    for (var j = 0; j < options.length; j++) {
      var temp = {}
      temp.name = options[j].optionname
      temp.data = options[j].answers
      //temp.format = function(){return 0}
      pieseriae.push(temp)
    }
    new Charts({
      canvasId: quess[index].tid,
      type: 'pie',
      series: pieseriae,
      width: 340,
      height: 270,
      dataLabel: true,
    })
  },
  //显示饼图
  openPei: function (event) {   
    var that = this
    var index = event.currentTarget.dataset.index
    var quess = that.data.questions
    if (quess[index].showpei) {
      quess[index].showpei = false
    } else {
      quess[index].showpei = true
    }
    that.setData({
      questions: quess
    })
    that.drawPie(index)
  },

  //计算选项的选择人数占总人数的多少
  compute_proportion: function() {
    var that = this
    var quess = that.data.questions
    for (var i = 0; i < quess.length; i++) {
      //var total = 100   //设置一个总数，当选项为最后一个选项时，拿总数减其他选项的占比，得到其占比
      if (quess[i].type == 3) {//如果该题为简答题，则不用计算
        continue
      }else{
        //设置是否显示饼图标志
        quess[i].showpei=false
      }
      for (var j = 0; j < quess[i].options.length; j++) {
        var temp = quess[i].options[j].answers
        var proportion = (temp / quess[i].answers * 100).toFixed()//利用tofixed函数进行四舍五入
        // if (j == quess[i].options.length - 1) {
        //   quess[i].options[j].proportion = total
        // } else {
          quess[i].options[j].proportion = proportion
        // }
        //total = total - proportion
      }
    }
    that.setData({
      questions: quess
    })
    console.log(quess)
  },
  //对选项进行排序，通过sortflag判断是升序还是降序
  changeOptionSort: function(event) {
    var that = this
    var flag = that.data.sortflag   //获得升降标志
    var index = event.currentTarget.dataset.index //获得需要排序的题目在数组中的下标，值来自页面
    var quess = that.data.questions    //获得所有问题数组
    if(quess[index].type!=3){ 
      if (flag == 1) {   //升序
        quess[index].options.sort(function(a, b){
          return a.proportion - b.proportion
        })
        flag = 2
      }else{    //降序
        quess[index].options.sort(function (a, b) {
          return b.proportion - a.proportion
        })
        flag = 1
      }
    }
    
    console.log(flag)
    that.setData({
      questions: quess,
      sortflag: flag
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function(options) {
    console.log(options)
    var quesTemp = decodeURIComponent(options.quesNaire)
    this.setData({
      quesNaire: JSON.parse(quesTemp)
    })
    console.log(this.data.quesNaire)
    this.readQuestions()
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function() {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function() {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function() {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function() {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function() {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function() {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function() {

  }
})