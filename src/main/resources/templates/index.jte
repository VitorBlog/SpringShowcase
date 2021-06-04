<html>

    <head>

        <title>Twitter</title>

        <link rel="preconnect" href="https://fonts.gstatic.com">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@100;400;700&display=swap" rel="stylesheet">
        <script src="https://kit.fontawesome.com/14803720e1.js" crossorigin="anonymous"></script>
        <link rel="stylesheet" href="/main.css">

    </head>

    <body>

        <div class="content">

            <textarea placeholder="I'm alone and need attention from random people in social media."></textarea>

            <div class="send photo" onclick="selectImage()">
                <i class="fas fa-images"></i>
            </div>
            <div class="send">
                Enviar
            </div>

            <div class="posts"></div>

        </div>

    </body>

    <script src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.29.1/moment.min.js"></script>
    <script src="https://rawgit.com/leizongmin/js-xss/master/dist/xss.js"></script>

    <script>

        var image = undefined;

        $.ajax({
            url: '/api/posts',
            success: (data) => {

                data.forEach(

                    (post) => {

                        addPost(post);

                    }

                );
                updateDates();

            }
        });

        $('.send').click(function () {

            if ($('textarea').val() == '') return;
            publish($('textarea').val());

        });

        function vote(element) {

            let postId = $(element).parent().attr('id');

            $.post({
                url: '/api/vote',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify({
                    'post': postId,
                }),
                success: (data) => {
                    console.log($(element).children())
                    $(element).children().toggleClass('voted');
                },
                error: (data) => {
                    location.href = '/login';
                }
            });

        }

        function updateDates() {

            $(".date").each(function() {
                
                $(this).text(moment(Number($(this).attr('time'))).fromNow());

            });

            setTimeout(() => {
                updateDates();
            }, 5000);

        }

        function addPost(post) {

            $('.posts').html('<div class="post" id="' + post.id + '"><p>' + filter(post.description) + '</p>' + (post.image != null? '<img src="' + post.image + '">' : '') + '<br><div class="vote" onclick="vote(this)"><i class="' + (post.userVote? 'voted' : '') + ' far fa-heart"></i></div><p class="date" time="' + post.created + '"></p><p class="user">' + filter(post.author.name) + ' <span>@' + filter(post.author.username) + '</span></p></div>' + $('.posts').html());

        }

        function publish(string) {

            $.post({
                url: '/api/post',
                contentType: 'application/json; charset=utf-8',
                data: JSON.stringify({
                    'description': string,
                    'image': image
                }),
                success: (data) => {
                    $('textarea').val('');
                    addPost(data);
                },
                error: (data) => {
                    location.href = '/login';
                }
            });

        }

        function filter(string) {

            return filterXSS(
                string,
                { whiteList: [] }
            );

        }

        function selectImage() {

            image = window.prompt('Coloque o link de uma imagem.', '');


        }

    </script>

</html>