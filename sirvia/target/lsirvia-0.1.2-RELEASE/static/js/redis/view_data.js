/**
 * 保存striing类型的值
 */
function saveString() {
    let content = $("textarea[name=content]").val();
    $.ajax({
        url: "/admin/redis/save_string",
        type: "post",
        data: {
            key: CURRENTKEY,
            content: content,
        },
        success(args) {
            if (args === "1") {
                layer.msg("保存成功", {icon: 1});
            } else {
                layer.msg("保存失败", {icon: 5});
            }
        }
    });
}

/**
 * 重新设置键值
 */
function reloadString() {
    layer.open({
        content: `<input type="text" name="new_key" autocomplete="off" class="layui-input" value="${CURRENTKEY}">`,
        btn: ['更新', '关闭'],
        offset: "150px",
        title: "更新" + CURRENTKEY + "的键值",
        shade: 0.8,
        zIndex: 200,
        type: 1,
        yes: function (index, layero) {
            // 获取新的键名
            let newKey = $("input[name=new_key]").val();
            $.ajax({
                url: "/admin/redis/reload_key_string",
                type: "post",
                data: {
                    key: CURRENTKEY,
                    newKey,
                },
                success(args) {
                    if (args.flag) {
                        parent.layer.title(newKey + "的查询结果", panelIndex);
                        layer.msg(args.msg, {icon: 1});
                        layer.close(index);
                        // 更新键值
                        CURRENTKEY = newKey;
                    }
                }
            });
        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}

/**
 * 删除键值对
 */
function remove() {
    layer.confirm('确认删除?', {icon: 3, title: '删除' + CURRENTKEY}, function (index) {
        //do something
        $.ajax({
            url: "/admin/redis/remove",
            type: "post",
            data: {
                key: CURRENTKEY,
            },
            success(args) {
                if (args === "1") {
                    layer.msg("删除成功", {icon: 1});
                    location.reload();
                } else {
                    layer.msg("删除失败", {icon: 5});
                }
            }
        });
        layer.close(index);
    });
}

/**
 * list删除选中的项
 */
function removeLine() {
    // 获取所有选中的列
    let checkList = [];
    $("input[name=row_select]:checked").each((index, item) => {
        checkList.push($(item).val());
    });
    layer.confirm('确认删除选中的行?', {icon: 3, title: '删除选中'}, function (index) {
        $.ajax({
            url: "/admin/redis/remove_line",
            type: "post",
            data: {
                key: CURRENTKEY,
                list: checkList,
            },
            success(args) {
                if (args === "1") {
                    layer.msg("删除成功", {icon: 1});
                    layer.close(index);
                    layer.close(panelIndex);
                    reloadData();
                } else {
                    layer.msg("删除失败", {icon: 5});
                }
            }
        });
        layer.close(index);
    });
}

/**
 * 添加新的一行到list
 */
function addLine() {
    layer.open({
        content: `<textarea name='new_line' class='layui-textarea'></textarea>`,
        btn: ['添加', '关闭'],
        area: "500px",
        offset: "150px",
        title: "插入新的一行",
        shade: 0.8,
        zIndex: 200,
        type: 1,
        yes: function (index, layero) {
            // 获取内容
            let content = $("textarea[name=new_line]").val();
            if (content.trim() !== "") {
                $.ajax({
                    url: "/admin/redis/add_line_list",
                    type: "post",
                    data: {
                        content,
                        key: CURRENTKEY,
                    },
                    success(args) {
                        if (args === "1") {
                            layer.msg("添加成功", {icon: 1});
                            layer.close(index);
                            // 先关闭原来的数据展示框
                            layer.close(panelIndex);
                            // 重新请求数据
                            reloadData();
                        } else {
                            layer.msg("添加失败", {icon: 5});
                        }
                    }
                });
            } else {
                layer.msg("内容不能为空", {icon: 5});
            }
        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}

/**
 * 重新加载数据
 */
function reloadData() {
    $.ajax({
        url: "/admin/redis/get_data",
        type: "post",
        data: {
            key: CURRENTKEY,
        },
        dataType: "JSON",
        success(args) {
            if (args.flag) {
                panelIndex = layer.open({
                    content: `${args.content}`,
                    zIndex: 1,
                    type: 1,
                    btn: ['关闭'],
                    offset: "150px",
                    title: CURRENTKEY + "的查询结果",
                    area: "50%",
                    yes: function (index, layero) {
                        layer.close(index);
                    }
                });
                table.init('table-filter', {
                    limit: 100000000000000000000,
                });
            }
        },
    });
}

/**
 * 删除set中的某个值
 */
function removeLineSet() {
    // 获取所有选中的列
    let checkList = [];
    $("input[name=row_select]:checked").each((index, item) => {
        checkList.push($(item).val());
    });
    layer.confirm('确认删除选中的行?', {icon: 3, title: '删除选中'}, function (index) {
        $.ajax({
            url: "/admin/redis/remove_line_set",
            type: "post",
            data: {
                key: CURRENTKEY,
                list: checkList,
            },
            success(args) {
                if (args === "1") {
                    layer.msg("删除成功", {icon: 1});
                    layer.close(index);
                    layer.close(panelIndex);
                    reloadData();
                } else {
                    layer.msg("删除失败", {icon: 5});
                }
            }
        });
        layer.close(index);
    });
}

/**
 * 添加一行新的数据到set
 */
function addLineSet() {
    layer.open({
        content: `<textarea name='new_line' class='layui-textarea'></textarea>`,
        btn: ['添加', '关闭'],
        area: "500px",
        offset: "150px",
        title: "插入新的一行",
        shade: 0.8,
        zIndex: 200,
        type: 1,
        yes: function (index, layero) {
            // 获取内容
            let content = $("textarea[name=new_line]").val();
            if (content.trim() !== "") {
                $.ajax({
                    url: "/admin/redis/add_line_set",
                    type: "post",
                    data: {
                        content,
                        key: CURRENTKEY,
                    },
                    success(args) {
                        if (args === "1") {
                            layer.msg("添加成功", {icon: 1});
                            layer.close(index);
                            layer.close(panelIndex);
                            reloadData();
                        } else {
                            layer.msg("添加失败", {icon: 5});
                        }
                    }
                });
            } else {
                layer.msg("内容不能为空", {icon: 5});
            }
        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}

/**
 * 删除zset中的选中行
 */
function removeLineZset() {
    // 获取所有选中的列
    let checkList = [];
    $("input[name=row_select]:checked").each((index, item) => {
        checkList.push($(item).val());
    });
    layer.confirm('确认删除选中的行?', {icon: 3, title: '删除选中'}, function (index) {
        $.ajax({
            url: "/admin/redis/remove_line_zset",
            type: "post",
            data: {
                key: CURRENTKEY,
                list: checkList,
            },
            success(args) {
                if (args === "1") {
                    layer.msg("删除成功", {icon: 1});
                    layer.close(index);
                    layer.close(panelIndex);
                    reloadData();
                } else {
                    layer.msg("删除失败", {icon: 5});
                }
            }
        });
        layer.close(index);
    });
}

/**
 * 为zset添加一行数据
 */
function addLineZset() {
    layer.open({
        content: `
                <div>分数：</div>
                <input type="text" name="score" autocomplete="off" class="layui-input">
                <div>内容：</div>
                <textarea name='new_line' class='layui-textarea'></textarea>
            `,
        btn: ['添加', '关闭'],
        area: "500px",
        offset: "150px",
        title: "插入新的一行",
        shade: 0.8,
        zIndex: 200,
        type: 1,
        yes: function (index, layero) {
            // 获取分数
            let score = $("input[name=score]").val().trim();
            // 获取内容
            let content = $("textarea[name=new_line]").val();
            if (content.trim() !== "") {
                $.ajax({
                    url: "/admin/redis/add_line_zset",
                    type: "post",
                    data: {
                        content,
                        score,
                        key: CURRENTKEY,
                    },
                    success(args) {
                        if (args === "1") {
                            layer.msg("添加成功", {icon: 1});
                            layer.close(index);
                            layer.close(panelIndex);
                            reloadData();
                        } else {
                            layer.msg("添加失败", {icon: 5});
                        }
                    }
                });
            } else {
                layer.msg("内容不能为空", {icon: 5});
            }
        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}

/**
 * 删除hash中选中的行
 */
function removeLineHash() {
    // 获取所有选中的列
    let checkList = [];
    $("input[name=row_select]:checked").each((index, item) => {
        checkList.push($(item).val());
    });
    layer.confirm('确认删除选中的行?', {icon: 3, title: '删除选中'}, function (index) {
        $.ajax({
            url: "/admin/redis/remove_line_hash",
            type: "post",
            data: {
                key: CURRENTKEY,
                list: checkList,
            },
            success(args) {
                if (args === "1") {
                    layer.msg("删除成功", {icon: 1});
                    layer.close(index);
                    layer.close(panelIndex);
                    reloadData();
                } else {
                    layer.msg("删除失败", {icon: 5});
                }
            }
        });
        layer.close(index);
    });
}

/**
 * 添加新行到hash
 */
function addLineHash() {
    layer.open({
        content: `
                <div>键名：</div>
                <input type="text" name="key" autocomplete="off" class="layui-input">
                <div>键值：</div>
                <textarea name='new_line' class='layui-textarea'></textarea>
            `,
        btn: ['添加', '关闭'],
        area: "500px",
        offset: "150px",
        title: "插入新的一行",
        shade: 0.8,
        zIndex: 200,
        type: 1,
        yes: function (index, layero) {
            // 获取分数
            let hashKey = $("input[name=key]").val().trim();
            // 获取内容
            let content = $("textarea[name=new_line]").val();
            if (content.trim() !== "") {
                $.ajax({
                    url: "/admin/redis/add_line_hash",
                    type: "post",
                    data: {
                        content,
                        hashKey,
                        key: CURRENTKEY,
                    },
                    success(args) {
                        if (args === "1") {
                            layer.msg("添加成功", {icon: 1});
                            layer.close(index);
                            layer.close(panelIndex);
                            reloadData();
                        } else {
                            layer.msg("添加失败", {icon: 5});
                        }
                    }
                });
            } else {
                layer.msg("内容不能为空", {icon: 5});
            }
        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}

/**
 * 修改列表的某个值
 * @param ind
 * @param val
 * @param self
 */
function modifyList(ind, val, self) {
    layer.open({
        content: `
                <div>修改值：</div><br>
                <input type="text" name="value" autocomplete="off" class="layui-input" value="${val}">
            `,
        btn: ['确认', '关闭'],
        area: "500px",
        offset: "150px",
        title: "修改list的值",
        shade: 0.8,
        zIndex: 200,
        type: 1,
        yes: function (index, layero) {
            // 获取修改后的值
            let value = $("input[name=value]").val();
            if (val === value) {
                layer.msg("未被修改", {icon: 5});
                return null;
            }
            if (value.trim() === "") {
                layer.msg("不能为空", {icon: 5});
                return null;
            }
            $.ajax({
                url: "/admin/redis/modify_list",
                type: "post",
                data: {
                    key: CURRENTKEY,
                    index: ind,
                    value: value
                },
                success(args) {
                    if (args === "1") {
                        layer.close(index);
                        $(self).parents("tr").find("td:eq(1) div").html(value);
                        layer.msg("修改成功", {icon: 1});
                    }
                }
            });
        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}

/**
 * 修改set的某个值
 * @param val
 * @param self
 */
function modifySet(val, self) {
    layer.open({
        content: `
                <div>修改值：</div><br>
                <input type="text" name="value" autocomplete="off" class="layui-input" value="${val}">
            `,
        btn: ['确认', '关闭'],
        area: "500px",
        offset: "150px",
        title: "修改set的值",
        shade: 0.8,
        zIndex: 200,
        type: 1,
        yes: function (index, layero) {
            // 获取修改后的值
            let value = $("input[name=value]").val();
            if (val === value) {
                layer.msg("未被修改", {icon: 5});
                return null;
            }
            if (value.trim() === "") {
                layer.msg("不能为空", {icon: 5});
                return null;
            }
            $.ajax({
                url: "/admin/redis/modify_set",
                type: "post",
                data: {
                    key: CURRENTKEY,
                    oldValue: val,
                    newValue: value
                },
                success(args) {
                    if (args === "1") {
                        layer.close(index);
                        $(self).parents("tr").find("td:eq(1) div").html(value);
                        layer.msg("修改成功", {icon: 1});
                    }
                }
            });
        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}

/**
 * 修改zset的某个值
 * @param val
 * @param score
 * @param self
 */
function modifyZset(val, score, self) {
    layer.open({
        content: `
                <div>修改分数：</div><br>
                <input type="text" name="m_score" autocomplete="off" class="layui-input" value="${score}">
                <div>修改值：</div><br>
                <input type="text" name="m_value" autocomplete="off" class="layui-input" value="${val}">
            `,
        btn: ['确认', '关闭'],
        area: "500px",
        offset: "150px",
        title: "修改zset的值",
        shade: 0.8,
        zIndex: 200,
        type: 1,
        yes: function (index, layero) {
            // 获取修改后的值
            let value = $("input[name=m_value]").val();
            let sr = $("input[name=m_score]").val();
            if (val === value && sr === score) {
                layer.msg("未被修改", {icon: 5});
                return null;
            }
            if (value.trim() === "" || sr.trim() === "") {
                layer.msg("不能为空", {icon: 5});
                return null;
            }
            $.ajax({
                url: "/admin/redis/modify_zset",
                type: "post",
                data: {
                    key: CURRENTKEY,
                    oldValue: val,
                    newValue: value,
                    score: sr,
                },
                success(args) {
                    if (args === "1") {
                        layer.close(index);
                        $(self).parents("tr").find("td:eq(1) div").html(value);
                        $(self).parents("tr").find("td:eq(2) div").html(sr);
                        layer.msg("修改成功", {icon: 1});
                    }
                }
            });
        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}

/**
 * 修改hash的某个值
 * @param key
 * @param val
 * @param self
 */
function modifyHash(key, val, self) {
    layer.open({
        content: `
                <div>修改键：</div><br>
                <input type="text" name="h_key" autocomplete="off" class="layui-input" value="${key}">
                <div>修改值：</div><br>
                <input type="text" name="h_value" autocomplete="off" class="layui-input" value="${val}">
            `,
        btn: ['确认', '关闭'],
        area: "500px",
        offset: "150px",
        title: "修改hash的值",
        shade: 0.8,
        zIndex: 200,
        type: 1,
        yes: function (index, layero) {
            // 获取修改后的值
            let k = $("input[name=h_key]").val();
            let v = $("input[name=h_value]").val();
            if (k === key && v === val) {
                layer.msg("未被修改", {icon: 5});
                return null;
            }
            if (k.trim() === "" || v.trim() === "") {
                layer.msg("不能为空", {icon: 5});
                return null;
            }
            $.ajax({
                url: "/admin/redis/modify_hash",
                type: "post",
                data: {
                    key: CURRENTKEY,
                    oldField: key,
                    newField: k,
                    value: v,
                },
                success(args) {
                    if (args === "1") {
                        layer.close(index);
                        $(self).parents("tr").find("td:eq(1) div").html(k);
                        $(self).parents("tr").find("td:eq(2) div").html(v);
                        layer.msg("修改成功", {icon: 1});
                    }
                }
            });
        },
        btn2: function (index, layero) {
            layer.close(index);
        }
    });
}

// 拉出或者收起侧边栏
function toggle() {
    // 获取panel的宽度
    let w = $("#add .panel").width();
    $("#add .panel").css("width", (236 - w) + "px");
    if (w === 0) {
        $("#add button i").removeClass("layui-icon-right").addClass("layui-icon-left");
    } else {
        $("#add button i").removeClass("layui-icon-left").addClass("layui-icon-right");
    }
}
