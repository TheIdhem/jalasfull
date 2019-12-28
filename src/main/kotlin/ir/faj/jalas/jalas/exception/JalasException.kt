package ir.faj.jalas.jalas.exception

class RoomNotAvailable(message: String) : RuntimeException(message) {
    constructor() : this("اتاق مورد نظر رزرو شده")
}

class InternalServerError(message: String) : RuntimeException(message) {
    constructor() : this("مشکلی در گرفتن اتاق به وجود آمده بعدا چک میشود و با ایمیل اطلاع داده میشود")
}

class InternalServerErr(message: String) : RuntimeException(message) {
    constructor() : this("مشکلی در گرفتن اتاق به وجود آمده دوباره تلاش کنید")
}

class BadRequest(message: String) : RuntimeException(message) {
    constructor() : this("تاریخ شروع یا تاریخ پایان نا معتبر میباشد")
}

class NotFoundRoom(message: String) : RuntimeException(message) {
    constructor() : this("اتاق مورد نظر یافت نشد")
}

class NotFoundUser(message: String) : RuntimeException(message) {
    constructor() : this("کاربر مورد نظر یافت نشد")
}

class NotFoundPassword(message: String) : RuntimeException(message) {
    constructor() : this("رمز مورد نظر یافت نشد")
}

class UsernameAlreadyReserved(message: String) : RuntimeException(message) {
    constructor() : this("نام کاربری مورد نظر در سیستم موجود میباشد")
}

class UserNotAllowToChange(message: String) : RuntimeException(message) {
    constructor() : this("شما اجازه‌ی ایجاد تغیرات را ندارید")
}

class NotFoundVote(message: String) : RuntimeException(message) {
    constructor() : this("رای مورد نظر موجود نبود")
}

class NotAllowToAccess(message: String) : RuntimeException(message) {
    constructor() : this("شما در این جلسه حظور ندارید و اجازه ندارید اطلاعات آن را ببنید")
}


