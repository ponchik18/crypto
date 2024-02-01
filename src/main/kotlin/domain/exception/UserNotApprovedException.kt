package domain.exception

import java.util.UUID

class UserNotApprovedException(
    userId: UUID
): RuntimeException("User with id '$userId' not approved!")