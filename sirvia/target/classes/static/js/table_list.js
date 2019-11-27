/*
列表展示页面的js
 */
$(() => {
    $("#batch_create_data").click(() => {
        // 获取所有的列
        let modelName = $("input[name=model_name]").val();
        let beanName = $("input[name=bean_name]").val();
        $.ajax({
            url: `/admin/get_field/${modelName}/${beanName}`,
            type: "post",
            dataType: "JSON",
            success(args) {
                // 填充的内容
                let content = `
                    <h3 style="margin-bottom: 20px;">请先进行配置</h3>
                    <div class="layui-form-item">
                        <label class="layui-form-label" style="width: 150px; padding-left: 0; text-align: left;">
                            批量生成数据的条数:
                        </label>
                        <div class="layui-input-inline">
                            <input type="number" name="data_nums" min="1" max="1000" step="1"
                            required lay-verify="required" placeholder="请输入要生成的数据数" autocomplete="off" class="layui-input">
                        </div>
                        <span class="layui-word-aux">数据只能是整数，而且只能是1~1000的数</span>
                    </div>
                    <hr class="layui-bg-red">
                    <h3 style="margin-bottom: 20px;">请选择要填充的字段 <small>(不能不选)</small></h3>
                    <div class="layui-form"><div class="layui-form-item">
                    <div class="alert alert-success" role="alert">
                `;
                for (let ind in args) {
                    let item = args[ind + ''];
                    content += `<input type="checkbox" name="${item}" title="${item}">`;
                }
                content += '</div></div></div>';

                layer.open({
                    title: "批量生成数据",
                    content,
                    skin: "layui-layer-molv",
                    offset: "150px",
                    btnAlign: "r",
                    shade: 0.8,
                    anim: 5,
                    resize: false,
                    move: false,
                    tips: [1, '#c00'],
                    area: '600px',
                    closeBtn: 2,
                    btn: ['执行', '取消'],
                    yes: function (index, layero) {
                        alert(8867);
                    },
                });
                layui.use('form', function(){
                    let form = layui.form;
                    //根据的type类型修改
                    form.render('checkbox');
                });
                $(".layui-icon-ok").css({
                    height: "30px",
                    width: "31px",
                });
            }
        });
    });
});


