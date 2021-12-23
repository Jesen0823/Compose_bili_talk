package com.jesen.biliwebview_module.debug.localprovidertest

import androidx.compose.runtime.staticCompositionLocalOf

/***
Compose通过参数的方式向可组合函数显式地通过组合树传递数据。这通常是让数据流通过树的最简单和最好的方法。
有时，这个模型可能很麻烦，或者由于许多组件需要数据，或者当组件需要在彼此之间传递数据但保持实现细节私有时，这个模型可能会被分解。对于这些情况，CompositionLocals可以作为一种隐式的方式来让数据流通过一个组合。
当地人的天性是分等级的。当compostionlocal的值需要被限定为组合的特定子层次结构时，它们是有意义的。
必须创建一个CompositionLocal实例，它可以被使用者静态地引用。compontionlocal实例本身不包含数据，可以将其视为向下传递到树中的数据的类型安全标识符。compontionlocal工厂函数只接受一个参数:在没有提供程序的情况下使用compontionlocal时，这个工厂将创建一个默认值。如果您不愿意处理这种情况，则可以在此工厂中抛出错误*/


//val ActiveBook = compositionLocalOf<Book> { error("No active book found!") }


val ActiveBook = staticCompositionLocalOf<Book> {
    error("No active book found!")
}

