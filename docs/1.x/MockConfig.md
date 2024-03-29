# 配置说明

`MockConfig`是构造器的配置，用来控制数据构造流程。合理地使用`MockConfig`可以最大程度地满足开发者的需求。

需要注意的是，在构建流程中，__忽略设置__ 优先度 __最高__，哪怕已经指定了数据构建器也不会执行构建。
__级联设定__ 优先度 __最低__，如果有对应的数据构建器，则会执行构建器的构建而不会进行级联构建。

## 引用深度

为了避免在循环引用导致的栈溢出问题，这里使用了引用深度设置，用来控制创建深度。

- `setCreatingDepth(int)`

## 忽略未知属性

默认情况下，当创建器发现没有构建器的属性时，会抛出无法匹配的异常。可以通过设置来忽略此异常并尝试无参构建。

- `ignoredUnknownField()`

## 设置允许的修饰符

默认情况下只允许构造仅包含`private`修饰符的属性，但为了做测试控制，这里允许开发者自定义修饰符限制。

- `setAllowPrivate(boolean)`、`setAllowPublic(boolean)`、`setAllowProtect(boolean)`、`setAllowStatic(boolean)`
  - 这里提供了四个基础修饰符控制，方便开发者直接调用。
- `setAllowedModifiers(int)`、`addAllowedModifiers(int... )`、`removeAllowedModifiers(int...)`
  - 如果四个基础修饰符不够，开发者可以通过此方法直接设定允许的修饰符。

在进行修饰符判定时，会进行排除判定。也就是如果属性至少有一个没有被允许的修饰符，则忽略此属性的构建。

## 设置通用数组大小

在进行数组填充时，如果数组没有进行初始化，则数组大小就会由配置决定。

- `setArraySize(int)`

## ~~强制新建对象~~

~~在默认情况下，当属性有值时则会跳过此属性的创建。可以通过设定强制创建来重新创建对象。~~

- ~~`setForceNew(boolean)`~~

## 设定属性生成选项

在默认情况下，构建器会对每一个值进行生成并替换。
通过`FieldOption`来设定是否忽略已存在的值或是否只针对基础类型做处理。

- `appendFieldOption(int)`

允许的值如下：

- `ALLOWED_NULL` - 允许对空值属性生成值
- `ALLOWED_NOTNULL` - 允许对非空值属性生成值
- `ALLOWED_PRIMITIVE` - 忽略值是否非空，都重新生成值
- `ALLOWED_CLASS` - 允许对class类型的属性生成值
- `ALLOWED_PRIMITIVE` - 允许对基础类型的属性生成值
- `IGNORED_TYPE` - 忽略属性类型，都重新生成值

## 添加指定属性的构建器

需要对特定的属性进行单独处理时可以通过此方法进行构建器绑定。

- `addFieldCreator([SFunction|String|Class,], DataCreator)`

## 添加实例构建器

当存在无法直接新建的对象或需要在创建对象后特殊处理时，可以添加其对应的实例构建器。

- `addInstanceCreator(InstanceCreator)`

## 设定自动级联构建

如果不需要单独指定需要级联构建的类，可以使用自动级联构建，让构建器对所有类都进行级联构建。

默认情况 __false__

- `setAutoCascade(boolean)`

## 添加需要级联构建的属性

级联构建表示在创建此对象后，会继续构建此对象的内部属性

- `addCascadeCreateKey(SFunction|String|Class)`
- `addCascadeCreatePackage(String)`
  - 级联构建包下的所有类
- `addCascadeCreatePattern(String)`
  - 级联构建与正则表达式匹配的所有属性或类全名

## 添加需要忽略的属性

某些属性不需要或是不想要构建时可以通过此方法进行忽略

- `addIgnoredField(SFunction|Class)`
- `addIgnoredFieldPackage(String)`
  - 忽略包名下的所有类的构建
- `addIgnoredFieldRegex(String)`
  - 忽略与表达式相同的属性或类全名key
