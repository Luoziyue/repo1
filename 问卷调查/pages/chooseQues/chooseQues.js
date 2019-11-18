// pages/chooseQues/chooseQues.js
var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    radiovalue: 1,  //类型按钮的值  对应后台的question的type
    must_do: false,   //是否必做
    quesNaireId: 1,   //所属问卷的id
    oldQuestion: {},  //如果是编辑题目，此处因为原先题目的各项数据
    optionList: [],   //选项列表
    option: {  //选项
      'id': 0,
      'value': '',
      'queNaireId': null
      },
  },

  radioChange: function(event){
    console.log(event.detail.value)
    this.setData({
      radiovalue: event.detail.value
    })
  },
  saveQuestion: function(data){
    var temp = data.detail.value
    var that = this
    var list = that.data.optionList
    var optionList = []  //选项列表，只包含选项名
    for(var item in list){
      optionList.push(list[item].value)
    }
    console.log(data)
    console.log(temp)
    console.log(that.data.quesNaireId)//选项列表
    wx.request({
      url: app.globalData.href+'savaQuestion',
      data: {
        quesNaireId: that.data.quesNaireId,
        quesname: temp.quesname,
        must_do: temp.must_do,
        type: temp.type,
        optionList: optionList,
        tid: that.data.oldQuestion.tid  //问题id，如果为空，那么就是新建，如果有值，就会更新
      },
      header: app.globalData.header,
      method: 'POST',
      success: function(res){
        wx.navigateBack({
          
        })
      }
    })
  },
  addoption: function(optionName){
    var list = this.data.optionList
    var temp = JSON.stringify(this.data.option)
    var option = JSON.parse(temp)
    console.log(option)
    option.id += 1
    option.value=optionName
    list.push(option)
    this.setData({
      optionList: list,
      option: option
    })
  },
  deleteoption: function(event){
    console.log(event.currentTarget.dataset.optionid)
    var flag = false   //判断是否找到相应选项并删除的标志
    var optionid = event.currentTarget.dataset.optionid  //删除的选项id
    var list = this.data.optionList
    for(var i=0;i<list.length;i++){
      if(list[i].id == optionid){
        list.splice(i,1)
        flag = true
      }
      if (flag && i!=list.length) {
        list[i].id -= 1
      }
    }
    this.setData({
      optionList: list
    })
  },
  inputchange: function(event){
    var optionid = event.currentTarget.dataset.optionid //获得发生变化的input  id
    var list = this.data.optionList
    for (var i = 0; i < list.length; i++) {
      if (list[i].id == optionid) {
        list[i].value = event.detail.value  //在选项列表中找到该选项并修改其值
      }
    }
    this.setData({
      optionList: list
    })
  },

  clickAdd: function(){
    this.addoption('')
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options)
    this.setData({
      quesNaireId: options.quesNaireId,
      oldQuestion: JSON.parse(options.question)
    })
    var list_temp = this.data.oldQuestion.options
    if(list_temp!=null){//编辑的题目中有选项，把选项名传到addOption,添加带值的option
      for (var item in list_temp) {
        this.addoption(list_temp[item].optionname)
        console.log(list_temp[item].optionname)
      }
    }else{//在所编辑的题目中没有选项或是新添加一个题目  增加两个空的option
      this.addoption('')
      this.addoption('')
    }
    console.log(this.data.oldQuestion)
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