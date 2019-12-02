package ir.faj.jalas.jalas.configurations

import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class HttpResponseHeadersInterceptor : HandlerInterceptorAdapter() {

    override fun postHandle(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any,
            modelAndView: ModelAndView?) {
        super.postHandle(request, response, handler, modelAndView)
    }
}
