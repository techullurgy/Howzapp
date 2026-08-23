package com.techullurgy.howzapp.core.network.http.core

context(_: NetworkClient)
suspend inline fun <reified Response> httpGet(
    params: NetworkRequestParams.WithoutBody
): Response = httpConnector<Unit, Response>().get(params)

context(_: NetworkClient)
suspend inline fun <reified Request, reified Response> httpPost(
    params: NetworkRequestParams.WithBody<Request>
) : Response = httpConnector<Request, Response>().post(params)

context(_: NetworkClient)
suspend inline fun <reified Request, reified Response> httpPut(
    params: NetworkRequestParams.WithBody<Request>
) : Response = httpConnector<Request, Response>().put(params)

context(_: NetworkClient)
suspend inline fun <reified Request, reified Response> httpDelete(
    params: NetworkRequestParams.WithoutBody
) : Response = httpConnector<Request, Response>().delete(params)