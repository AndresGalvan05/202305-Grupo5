db.createUser(
    {
        user: "usr-check-mongo",
        pwd: "pwd-check-mongo",
        roles: [
            {
                role: "readWrite",
                db: "check-dev-mongo"
            }
        ]
    }
);