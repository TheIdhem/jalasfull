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


/*
def cms_after(response):
    response.headers['Access-Control-Allow-Credentials'] = 'true'
    response.headers['Access-Control-Allow-Origin'] = g.publishing_platform.panel_url
    response.headers['Cache-Control'] = "no-cache, max-age=0"
    response.headers['Access-Control-Allow-Headers'] = 'X-Requested-With, accept, content-type, origin, Authorization'
    response.headers['Access-Control-Expose-Headers'] = 'X-Requested-With, accept, content-type, origin, Authorization'
    return response


def admin_after(response):
    response.headers['Access-Control-Allow-Credentials'] = 'true'
    response.headers['Access-Control-Allow-Origin'] = PublishingPlatformType.mediaad.modir_url
    response.headers['Cache-Control'] = "no-cache, max-age=0"
    response.headers['Access-Control-Allow-Headers'] = 'X-Requested-With, accept, content-type, origin, Authorization'
    response.headers['Access-Control-Expose-Headers'] = 'X-Requested-With, accept, content-type, origin, Authorization'
    return response
*/