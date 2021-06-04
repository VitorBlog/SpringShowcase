<html>

    <head>

        <title>Twitter - Login</title>

        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@100;400;700&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/14803720e1.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="/main.css">
        <link rel="stylesheet" href="/login.css">

    </head>

    <body>

        <div class="content">

            <h2>Login</h2>
            <input type="text" name="username" placeholder="Usuario">
            <input type="password" name="password" placeholder="Senha">
            <input type="submit" value="Logar" onclick="login()"/>

        </div>

    </body>

    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>

    <script>

        function login() {

            $.post({
                url: '/api/auth',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify({
                    'username': $('input[name=username]').val(),
                    'password': $('input[name=password]').val()
                }),
                success: (data) => {
                    if (data.success) location.href = '/'; else alert(data);
                },
                error: (data) => {
                    alert(data);
                }
            });

        }

    </script>

</html>