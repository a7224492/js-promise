当代码中使用了泛型<T> T时，单纯的使用ant打包会编译不过，需要使用eclipse自带的几个jar。

将上述四个jar拷贝到ant_home/lib下，并在脚本中添加如下语句：<property name="build.compiler" value="org.eclipse.jdt.core.JDTCompilerAdapter"/> 表示使用该JDT，即可编译通过
