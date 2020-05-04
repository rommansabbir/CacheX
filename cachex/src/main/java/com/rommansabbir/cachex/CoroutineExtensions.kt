package com.rommansabbir.cachex

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.lang.reflect.InvocationTargetException
import java.security.GeneralSecurityException
import java.sql.SQLException
import java.text.ParseException


suspend fun executeCoroutine(
    body: suspend () -> Unit,
    onError: suspend (Exception) -> Unit
) {
    try {
        body()
    } catch (e: Exception) {
        onError.invoke(e)
    } catch (e: CancellationException) {
        onError.invoke(e)
    } catch (e: IOException) {
        onError.invoke(e)
    } catch (e: RuntimeException) {
        onError.invoke(e)
    } catch (e: ParseException) {
        onError.invoke(e)
    } catch (e: NullPointerException) {
        onError.invoke(e)
    } catch (e: ArithmeticException) {
        onError.invoke(e)
    } catch (e: NumberFormatException) {
        onError.invoke(e)
    } catch (e: IllegalArgumentException) {
        onError.invoke(e)
    } catch (e: IllegalArgumentException) {
        onError.invoke(e)
    } catch (e: ArrayIndexOutOfBoundsException) {
        onError.invoke(e)
    } catch (e: InvocationTargetException) {
        onError.invoke(e)
    } catch (e: SQLException) {
        onError.invoke(e)
    } catch (e: GeneralSecurityException) {
        onError.invoke(e)
    } catch (e: UnsupportedEncodingException) {
        onError.invoke(e)
    }
}


suspend fun notifyOnMain(body: suspend () -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        body.invoke()
    }
}