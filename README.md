# socket-variations

The followings are my opinions only.

We got three approachs to implement websocket service.
1. [Socket.io](https://socket.io/blog/native-socket-io-and-android/)
2. [Ktor websocket](https://ktor.io/docs/websocket-client.html)
3. [OkHttp WebSocket](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-web-socket/)

### Object creation

Socket.io
```kotlin
 IO.socket(socketUrl)
```

Ktor
```kotlin
HttpClient() { install(WebSockets) }
```

OkHttp
```kotlin
OkHttpClient.Builder()
            .build()
            .newWebSocket(request: Request, listener: WebSocketListener)
```

### Subscribing events and Emiting events

Socket.io
```kotlin
 socket.on(event: String,ev: EventListener)
```
For emitting, It's just
```kotlin
 socket.emit(event, jsonString)
```

Ktor WebSocket
```kotlin
client.webSocket(method = HttpMethod.Get, host = socketUrl, path = "/") {
            while (true) {
                val othersMessage = incoming.receive() as? Frame.Text
                // Parse your message here
            }
}
```
For emitting,
```kotlin
client.webSocket(method = HttpMethod.Get, host = socketUrl, path = "/") {
      send(event, jsonString)
}
```

OkHttp
We need to perform manual ws request using
```kotlin
val request = Request.Builder().url(socketUrl).build()
```
and then 
```kotlin
client.newWebSocket(request: Request, listener: WebSocketListener)
```
WebSocketListener is a long one. So, with the help of callbackflow
```kotlin
fun OkHttpClient.newWebSocketAsync(request: Request) = callbackFlow<WebSocketState> {
    newWebSocket(
        request,
        object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                trySend(WebSocketState.Open(webSocket, response))
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                trySend(WebSocketState.OnMessage(webSocket, text))
            }
        }
    )
}
```
You can stream the changes via flow using
```kotlin
client.newWebSocketAsync(request).collectLatest {}
```
But IMO, it would be long shot if we want to peform on, off, send, connect and other events

#### To conclude
- Socket.io is pretty straightforward and developer friendly IMHO
- Ktor web socket is just yet another option.
- OkHttp is just yet core library, but you need to tweak yourself to fit your requirements.
