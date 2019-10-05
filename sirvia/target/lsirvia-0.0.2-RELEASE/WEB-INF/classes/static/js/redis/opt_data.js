function addString() {
    layer.open({
        type: 0,
        title: '添加string',
        shade: 0.2,
        offset: '150px',
        area: '500px',
        content: `
                <div class="layui-form">
                    <div class="layui-form-item">
                        <div>键名：</div>
                        <input type="text" name="key" placeholder="请输入键名" autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-form-item layui-form-text">
                        <div>键值：</div>
                        <textarea name="value" placeholder="请输入键值" class="layui-textarea"></textarea>
                    </div>
                </div>
            `,
        btn: ['保存', '取消'],
        yes: function (index, layero) {
            // 获取键名
            let key = $("input[name=key]").val();
            let value = $("textarea[name=value]").val();
            $.ajax({
                url: "/admin/redis/save_string",
                type: "post",
                data: {
                    key,
                    content: value,
                },
                success(args) {
                    if (args === "1") {
                        layer.msg("添加成功", {icon: 1});
                        layer.close(index);
                    } else {
                        layer.msg("添加失败", {icon: 5});
                    }
                },
            });
        },
    });
}

function addList() {
    layer.open({
        type: 0,
        title: '添加list',
        shade: 0.2,
        offset: '150px',
        area: '500px',
        content: `
                <div class="layui-form">
                    <div class="layui-form-item">
                        <div>键名：</div>
                        <input type="text" name="key" placeholder="请输入键名" autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-form-item layui-form-text">
                        <div>键值：</div>
                        <textarea name="value" placeholder="请输入键值" class="layui-textarea"></textarea>
                    </div>
                </div>
            `,
        btn: ['保存', '取消'],
        yes: function (index, layero) {
            // 获取键名
            let key = $("input[name=key]").val();
            let value = $("textarea[name=value]").val();
            $.ajax({
                url: "/admin/redis/add_line_list",
                type: "post",
                data: {
                    key,
                    content: value,
                },
                success(args) {
                    if (args === "1") {
                        layer.msg("添加成功", {icon: 1});
                        layer.close(index);
                    } else {
                        layer.msg("添加失败", {icon: 5});
                    }
                },
            });
        },
    });
}

function addSet() {
    layer.open({
        type: 0,
        title: '添加set',
        shade: 0.2,
        offset: '150px',
        area: '500px',
        content: `
                <div class="layui-form">
                    <div class="layui-form-item">
                        <div>键名：</div>
                        <input type="text" name="key" placeholder="请输入键名" autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-form-item layui-form-text">
                        <div>键值：</div>
                        <textarea name="value" placeholder="请输入键值" class="layui-textarea"></textarea>
                    </div>
                </div>
            `,
        btn: ['保存', '取消'],
        yes: function (index, layero) {
            // 获取键名
            let key = $("input[name=key]").val();
            let value = $("textarea[name=value]").val();
            $.ajax({
                url: "/admin/redis/add_line_set",
                type: "post",
                data: {
                    key,
                    content: value,
                },
                success(args) {
                    if (args === "1") {
                        layer.msg("添加成功", {icon: 1});
                        layer.close(index);
                    } else {
                        layer.msg("添加失败", {icon: 5});
                    }
                },
            });
        },
    });
}

function addZset() {
    layer.open({
        type: 0,
        title: '添加zset',
        shade: 0.2,
        offset: '150px',
        area: '500px',
        content: `
                <div class="layui-form">
                    <div class="layui-form-item">
                        <div>键名：</div>
                        <input type="text" name="key" placeholder="请输入键名" autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-form-item">
                        <div>分数：</div>
                        <input type="text" name="score" placeholder="请输入分数" autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-form-item layui-form-text">
                        <div>键值：</div>
                        <textarea name="value" placeholder="请输入键值" class="layui-textarea"></textarea>
                    </div>
                </div>
            `,
        btn: ['保存', '取消'],
        yes: function (index, layero) {
            // 获取键名
            let key = $("input[name=key]").val();
            let score = $("input[name=score]").val().trim();
            let value = $("textarea[name=value]").val();
            $.ajax({
                url: "/admin/redis/add_line_zset",
                type: "post",
                data: {
                    key,
                    score,
                    content: value,
                },
                success(args) {
                    if (args === "1") {
                        layer.msg("添加成功", {icon: 1});
                        layer.close(index);
                    } else {
                        layer.msg("添加失败", {icon: 5});
                    }
                },
            });
        },
    });
}

function addHash() {
    layer.open({
        type: 0,
        title: '添加hash',
        shade: 0.2,
        offset: '150px',
        area: '500px',
        content: `
                <div class="layui-form">
                    <div class="layui-form-item">
                        <div>键名：</div>
                        <input type="text" name="key" placeholder="请输入键名" autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-form-item">
                        <div>hash键名：</div>
                        <input type="text" name="hash_key" placeholder="请输入键名" autocomplete="off" class="layui-input">
                    </div>
                    <div class="layui-form-item layui-form-text">
                        <div>键值：</div>
                        <textarea name="value" placeholder="请输入键值" class="layui-textarea"></textarea>
                    </div>
                </div>
            `,
        btn: ['保存', '取消'],
        yes: function (index, layero) {
            // 获取键名
            let key = $("input[name=key]").val();
            let hashKey = $("input[name=hash_key]").val();
            let value = $("textarea[name=value]").val();
            $.ajax({
                url: "/admin/redis/add_line_hash",
                type: "post",
                data: {
                    key,
                    hashKey,
                    content: value,
                },
                success(args) {
                    if (args === "1") {
                        layer.msg("添加成功", {icon: 1});
                        layer.close(index);
                    } else {
                        layer.msg("添加失败", {icon: 5});
                    }
                },
            });
        },
    });
}
