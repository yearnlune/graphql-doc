# GraphQL Doc
> Easily create Graphql documentation

## Installation

---

```kotlin
plugins {
    id("io.github.yearnlune.graphql.doc.directive.plugin") version "0.0.2"
}

configurations {
    implementation {
        isCanBeResolved = true
    }
}

dependencies {
    implementation("io.github.yearnlune.graphql.doc:graphql-doc-directive:0.0.2")
}
```

## Directives

### @intOrigin
This is used to specify that the value of an enum should be represented as a number.

```graphql
enum Status {
    UNKNOWN @intOrigin(value: -1)
    INACTIVE @intOrigin(value: 0)
    ACTIVE @intOrigin(value: 1)
}
```

### @stringOrigin
This is used to specify that the value of an enum should be represented as a string.

```graphql
enum Separator {
    COMMA @stringOrigin(value: ",")
    TAB @stringOrigin(value: "\t")
    SPACE @stringOrigin(value: " ")
}
```

### @default
This is used to specify the default value of an input field.

```graphql
input ParseInput {
    text: String
    separator: Separator = COMMA @default(value: "Separator.COMMA")
} 
``` 
### @group
Group documents by category.

```graphql
enum Separator @group(category: "Parse") {
    COMMA @stringOrigin(value: ",")
    TAB @stringOrigin(value: "\t")
    SPACE @stringOrigin(value: " ")
}

input ParseInput @group(category: "Parse") {
    text: String
    separator: Separator = COMMA @default(value: "Separator.COMMA")
}
```

