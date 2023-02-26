# KoTeX

Библиотека позволяет создавать LaTeX документы в декларативном стиле. Небольшой пример для понимания:

```kotlin
val title: String = "Текст в центре"
Center {
    Bold {
        Text(title)
    }
}
```

На данный момент библиотека находиться в разработке, реализована небольшая часть функционала.

## Пример

В качестве примера с использованием KoTeX написан [решатель транспортной задачи](example) линейного программирования, 
который можно найти в папке example.

# Основы работы с KoTeX

## LaTeX-функции

Все декларативные функции, производящие некоторый LaTeX код должны соблюдать несколько важных правил:
1. функция должна иметь модификатор `suspend`. LaTeX-функции будут вызываться из соответствующей документу корутины,
контекст которой содержит информацию о нем;
2. функция ничего не должна возвращать. Весь производимый LaTeX код добавляется в соответствующий документ с помощью 
вызова функции `Content(raw: CharSequence)`;
3. функция может принимать любые аргументы, в том числе и другие LaTeX функции;
4. функция должна быть помечена аннотацией `@LaTeX`.

Соответственно, реализации LaTeX-функции:

```kotlin
@LaTeX
suspend inline fun Wrapped(
    start: String,
    end: String,
    content: @LaTeX suspend () -> Unit
) {
    Content(
        raw = start
    )
    content()
    Content(
        raw = end
    )
}
```

Следующий код:

```kotlin
Wrapped(
    start = "{",
    end = "}"
) {
    Text("wrapped")
}
```

Добавит в документ следующий контент:

```text
{wrapped}
```

## Документ

Описанные выше LaTeX-функции модифицируют некоторый документ, представляемый объектом класса BaseDocument. При создании 
объекта необходимо передать название документа, которое должно быть уникальным, а также путь к директории, в которой 
будут храниться `.tex` файлы.

Путь к jar файлу с программой можно получить, вызвав функцию `PathUtils.getJarDirectoryPath`, которая принимает в
качестве параметра объект любого класса программы.

### Добавление контента

Содержимое документа можно добавлять в конец документа с помощью вызова метода `append`,
принимающей в качестве аргумента LaTeX-функцию.

```kotlin
val doc = BaseDocument(name = "test-doc", path = "/Users/user/documents/test-doc")
doc.append {
    Text("Hello, world!")
}
doc.close()
```

### Теги

Содержимое можно добавлять в документ по тегам. По умолчанию в документе присутствует два тега, один из которых – 
`preamble`, а другой с тем же названием, что и документ. Содержимое, добавляемое вместе с тегом, записывается в 
отдельный `.tex` файл, а затем (или сразу же) добавляется в основной документ при помощи команды `\input`. Важный 
нюанс: содержимое тега `preamble` по умолчанию добавляется в начало документа. Это необходимо, чтобы упростить работу
с объявлением об использовании внешних пакетов.
