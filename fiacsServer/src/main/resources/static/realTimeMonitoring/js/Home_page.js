/**
 * Created by Administrator on 2017/10/26.
 */


//    Xabin




//应急事件处置


//    地图切换buttn-start

//轮播推送隐藏功能
$(".scroll_tool_outbox").mouseenter(function(){
    $(".scroll_tool_outbox").addClass("scroll_tool_outbox_current")
});
$(".scroll_tool_outbox").mouseleave(function(){
    $(".scroll_tool_outbox").removeClass("scroll_tool_outbox_current")
})
//轨迹回放功能激活
$(".search_guiji").click(function () {
    $("#divRouteReview").show();
});
$(".close_playback").click(function () {
    $(".trajectory_box").hide();
});
$("#btnBuffer").click(function () {
    $("#divBufferSetting").show();
});
$(".total_chose_pl").click(function () {
    $(".total_chose_box").show()
})
$(".total_chose_fr").click(function () {
    $(".total_chose_box").hide()
})
//    Xabin_end










