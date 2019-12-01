package ir.faj.jalas.jalas.exception

class RoomNotAvailable(message: String) : Exception(message) {
    constructor() : this("اتاق مورد نظر رزرو شده")
}

class InternalServerError(message: String) : Exception(message) {
    constructor() : this("مشکلی در گرفتن اتاق به وجود آمده بعدا چک میشود و با ایمیل اطلاع داده میشود")
}

class InternalServerErr(message: String) : Exception(message) {
    constructor() : this("مشکلی در گرفتن اتاق به وجود آمده دوباره تلاش کنید")
}

class BadRequest(message: String) : Exception(message) {
    constructor() : this("تاریخ شروع یا تاریخ پایان نا معتبر میباشد")
}

class NotFoundRoom(message: String) : Exception(message) {
    constructor() : this("اتاق مورد نظر یافت نشد")
}