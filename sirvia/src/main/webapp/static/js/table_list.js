/*
列表展示页面的js
 */
$(() => {
    $("#batch_create_data").click(() => {
        // 获取所有的列
        let modelName = $("input[name=model_name]").val();
        let beanName = $("input[name=bean_name]").val();
        // promise链式调用
        new Promise((resolve, reject) => {
            $.ajax({
                url: `/admin/get_field/${modelName}/${beanName}`,
                type: "post",
                dataType: "JSON",
                success(args) {
                    resolve(args);
                },
                error(err) {
                    reject(err);
                },
            });
        }).then((args) => {
            p2(args);
        }).catch(err => {
            layer.msg(err, {
                icon: 5,
            });
        });

        /**
         * 获取当前对象的所有字段
         * @param args
         * @returns {Promise<any | never>}
         */
        function p2(args) {
            return new Promise((resolve, reject) => {
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
                        <span class="layui-word-aux">只能是整数，范围是1~1000</span>
                    </div>
                    <hr class="layui-bg-red">
                    <h3 style="margin-bottom: 20px;">请选择要填充的字段 <small>(不能不选)</small></h3>
                    <div class="layui-form"><div class="layui-form-item">
                    <div class="alert alert-warning" role="alert">
                `;
                for (let ind in args) {
                    let item = args[ind + ''];
                    content += `<input class="fields" type="checkbox" name="${item}" title="${item}">`;
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
                    type: 1,
                    resize: false,
                    move: false,
                    tips: [1, '#c00'],
                    area: '600px',
                    closeBtn: 2,
                    btn: ['执行', '取消'],
                    yes: function (index, layero) {
                        // 添加完成之后，点击执行
                        // 验证数据条数是否填写正确
                        let dataNumbers = $("input[name=data_nums]").val();
                        if (dataNumbers <= 0 || dataNumbers > 1000) {
                            layer.msg('数据超出范围', {
                                icon: 5,
                            });
                            return;
                        }
                        // 获取选中的字段
                        let fields = $(".fields:checked");
                        let fieldsList = [];
                        for (let i = 0; i < fields.length; i++) {
                            fieldsList.push($(fields[i + '']).attr("name"));
                        }
                        if (fieldsList.length <= 0) {
                            layer.msg('必须选则字段', {
                                icon: 2,
                            });
                            return;
                        }
                        // 所有数据均没有问题，传到后台
                        $.ajax({
                            url: "/admin/generate_data",
                            type: "post",
                            data: {
                                dataNumbers,
                                fieldsList,
                            },
                            success(args) {
                                resolve(args);
                            },
                            error(err) {
                                reject(err);
                            }
                        });
                    },
                });
                layui.use('form', function () {
                    let form = layui.form;
                    //根据的type类型修改
                    form.render('checkbox');
                });
                $(".layui-icon-ok").css({
                    height: "30px",
                    width: "31px",
                });
            }).then(args => {
                p3(args);
            }).catch(err => {
                layer.msg(err, {
                    icon: 5,
                });
            });
        }

        /**
         * 生成数据后的回调
         * @param args
         * @returns {Promise<any>}
         */
        function p3(args) {
            return new Promise((resolve, reject) => {
                console.log(args);
            });
        }
    });
});


