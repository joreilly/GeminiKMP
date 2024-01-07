Kotlin/Compose Multiplatform sample to demonstrate Gemini Generative AI APIs (text based queries for now). Uses [Gemini REST APIs](https://ai.google.dev/tutorials/rest_quickstart) from common code using Ktor.


Running on
* iOS 
* Android
* Desktop
* Web (Wasm)

Set your Gemini API key (`gemini_api_key`) in `local.properties`

Related posts:
* [Exploring use of Gemini Generative AI APIs in a Kotlin/Compose Multiplatform project](https://johnoreilly.dev/posts/gemini-kotlin-multiplatform/)


To do
* Use Compose Multiplatform library to display markdown returned (waiting on one that supports Wasm based Compose for Web)
* Allow uploading images and running queries on those.

## Screenshots

### iOS

![Simulator Screenshot - iPhone 15 Pro - 2023-12-31 at 12 54 13](https://github.com/joreilly/GeminiKMP/assets/6302/2fee2bf5-06e6-4864-9154-8f2982ef7929)

### Android
![Screenshot_20231231_125523](https://github.com/joreilly/GeminiKMP/assets/6302/55fd802a-720a-4dae-b9d5-b5915727daca)


### Wasm based Compose for Web

<img width="1129" alt="Screenshot 2023-12-31 at 13 01 02" src="https://github.com/joreilly/GeminiKMP/assets/6302/f128bf8f-499b-40e9-a4bd-0674aa0f9240">

### Compose for Desktop

<img width="945" alt="Screenshot 2023-12-31 at 14 34 28" src="https://github.com/joreilly/GeminiKMP/assets/6302/e2798cc2-2f1a-411e-a360-68b755c7686d">

