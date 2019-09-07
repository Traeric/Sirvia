![NO IMG](./photo/logo.png)



**完美嵌入项目，丢弃沉重的数据库操作软件，启动项目就可以对数据进行增删改查**

Java Admin是一套完备的在线增删改查系统，它舍弃了传统的通过Navicat或者PLSQL等软件操作数据库的方式，而是在项目内部嵌入了Java Admin，通过在网页端操作数据库，类似于Django的admin。

当Java Admin嵌入到项目中后，可以通过Generate类创建超级用户，通过账户跟密码登陆Java Admin，Java Admin使用shiro做用户认证，相对安全。登陆之后可以查看Java Admin的系统表，如果需要控制用户自己的表，需要将对应的实体类在Register类中进行注册，注册之后就可以登陆Java Admin对表进行增删改查的操作。

## 起步

### 1. 填写数据库信息

使用Java Admin第一步需要在`resources/db.properties`文件中写上数据库连接的信息，包括url、user跟pwd

### 2. 生成user表以及账户

想要进入Java Admin页面管理数据表需要登陆，那么首先需要创建账户，账户存储在数据库中，因此需要先创建用户表，具体的创建是在`Generate`类中完成的，`Generate`的位置在`admin`目录的`com.ericjin.javadmin.generate`包下，通过执行类里面的`generateUserTable()`方法来创建用户表。

创建用户表后就需要创建账户了，还是在`Generate`类下，执行`createUser()`方法按照要求填写即可创建账户。

### 3. 登陆Java admin

启动项目后，通过地址`http://127.0.0.1:8080/admin/login`可以进入Java Admin的登陆页面，如下：

![NO IMG](./photo/login.png)

### 4. 操作数据表

登陆后会进入首页，首页展示如下：

![NO IMG](./photo/index.png)

可以看到，首页有一个Panel，Panel的头部写着系统表，这是Java Admin系统自带的表，可以看到有一张用户表，这张表就是刚刚第二步通过`Generate`类创建的表，里面存储有用户信息，一般不是太会用到。那么如果想要操作自己的表应该怎么做呢？

来到项目中，可以看到一个`Register`类，具体位置在admin目录下的`com.ericjin.javadmin`包下，来到类中可以看到一个`userTableList()`方法，里面会实例化一个List，然后返回出去，如果想要操作自己的表，就需要在该方法中将自己表对应的java实体类的Class对象添加到这个列表中，例如：

```java
public List<Class> userTableList() {
    List<Class> list = new ArrayList<>();
    // 此处添加用户表信息
    list.add(Test.class);
    list.add(Article.class);
    list.add(Tags.class);
    return list;
}
```

在上面的代码中添加了三个实体类，注意添加的实体类一定要在数据库中有对应的表。

重启项目，然后来到首页：

![NO IMG](./photo/index-user.png)

#### 查询数据

可以看到在系统表下面有了一个用户表，里面有三条信息，正是刚刚添加的三个实体类，可以点进去进行查看，例如现在点进`Article`中：

![NO IMG](./photo/list.png)

#### 添加数据

点进去后就是关于Article的所有数据了，在这里面可以针对每一条数据进行修改，也可以添加Article，只需要点击绿色的按钮即可，点击后会出现下面的页面：

![NO IMG](./photo/add.png)

​	Java Admin会根据`Article`实体类中的所有私有属性进行表单渲染，表单类型会根据属性的类型来选择渲染，或input框，或select框，或radio框，或穿梭框等等。

​	如果在该实体类中有外键关联了其他的实体类，Java Admin会找到所关联的表的所有字段渲染成一个select框供选择，显示外键的属性上面需要标注`@ForeignKey`注解，在注解里面可以配置要显示的字段名以及要保存的外键value。如果是多对多，则会渲染成穿梭框，需要在属性上标注`@ManyToManyField`注解，注解里面的内容后面会详细说明。

​	在上图中可以看到不论是外键还是多对多的旁边都有一个添加按钮，点击这个按钮就可以往对应的关联表的中添加信息，例如上图中点击按钮就可以对tag表进行添加，如下：

![NO IMG](./photo/add_foreign.png)

在上图中的红框中就是添加tag的页面，填写后保存即可。

#### 修改跟删除数据

再回到数据展示页面，可以看到在每一条数据后面都有一个编辑按钮，点击进入：

![NO IMG](./photo/edit.png)

可以看到和添加表的页面基本没有差别，只是在每一个输入框中都已经填入了已经保存在数据库中的信息，还有就是在底部添加了一个删除按钮，点击删除就可以对该条数据进行删除，其他都与添加页面的功能保持一致。

#### Action

Action是一个可以对数据进行批量操作的功能，在数据展示页面，选中要批量操作的数据，然后在Action选框中选中要进行何种批量操作，最后点击执行即可。如下：

![NO IMG](./photo/action.png)

Java Admin系统只提供了一个批量删除的操作，如果需要实现其他的批量操作，可以在`Action`类中自己定义，关于自定义Action的具体内容会在后面说明。

## Java Admin注解

## Action

 