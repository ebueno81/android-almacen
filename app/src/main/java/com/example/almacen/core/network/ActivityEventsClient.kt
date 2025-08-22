package com.example.almacen.core.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener
import okhttp3.sse.EventSources
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class ActivityEventsClient(
    private val baseUrl: String,
    private val onProcessed: (activityId: Int, idIngreso: Int) -> Unit,
    private val onError: (Throwable) -> Unit = {}
) {
    // 👇 SSE es conexión larga: sin readTimeout + ping para mantener viva
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)           // NUNCA cortar por inactividad
        .retryOnConnectionFailure(true)
        .pingInterval(15, TimeUnit.SECONDS)             // ayuda a proxies/NAT
        .build()

    private var eventSource: EventSource? = null
    private var retries = 0

    fun connect() {
        // normaliza baseUrl para evitar //doble-slash
        val url = baseUrl.trimEnd('/') + "/activities/stream"

        val req = Request.Builder()
            .url(url)
            .header("Accept", "text/event-stream")       // 👈 explícito
            .build()

        // cierra si ya había uno
        close()

        eventSource = EventSources.createFactory(client)
            .newEventSource(req, object : EventSourceListener() {

                override fun onOpen(es: EventSource, response: Response) {
                    retries = 0
                    // Log.d("SSE", "open ${response.code}")
                }

                override fun onEvent(es: EventSource, id: String?, type: String?, data: String) {
                    try {
                        // Log.d("SSE", "event: $data")
                        val js = JSONObject(data)
                        val status = js.optString("status")
                        if (status == "PROCESSED") {
                            val activityId = js.optInt("activityId", -1)
                            val idIngreso  = js.optInt("idIngresoCreado", 0)
                            if (activityId > 0) onProcessed(activityId, idIngreso)
                        }
                    } catch (e: Exception) {
                        onError(e)
                    }
                }

                override fun onFailure(es: EventSource, t: Throwable?, response: Response?) {
                    onError(t ?: RuntimeException("SSE error ${response?.code}"))
                    // reconexión simple con backoff
                    es.cancel()
                    eventSource = null
                    val delay = (1000L * (retries + 1)).coerceAtMost(15000L)
                    retries++
                    Thread {
                        try { Thread.sleep(delay) } catch (_: InterruptedException) {}
                        connect()
                    }.start()
                }
            })
    }

    fun close() {
        eventSource?.cancel()
        eventSource = null
    }
}
