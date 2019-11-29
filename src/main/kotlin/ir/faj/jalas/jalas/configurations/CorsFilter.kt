package ir.faj.jalas.jalas.configurations


import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by r on 5/9/18.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter : Filter {

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val response = res as HttpServletResponse
        val request = req as HttpServletRequest
        response.setHeader("Access-Control-Allow-Origin", req.getHeader("origin"))
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE")
        response.setHeader("Access-Control-Allow-Headers", "x-auth-token, x-requested-with, content-type, accept, origin, referer, Authorization")
        response.setHeader("Access-Control-Expose-Headers", "x-requested-with")
        response.setHeader("Access-Control-Allow-Credentials", "trxue")
        if ("OPTIONS" != request.method) {
            chain.doFilter(req, res)
        }
    }

    override fun init(filterConfig: FilterConfig) {}

    override fun destroy() {}

}
