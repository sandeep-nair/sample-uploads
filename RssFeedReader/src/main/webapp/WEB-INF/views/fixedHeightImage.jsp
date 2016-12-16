<%-- 
    Document   : fixedHeightImage
    Created on : Oct 25, 2016, 3:25:59 PM
    Author     : sandeep.s
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <title>Css aspect ratio to have images of same height</title>
        <meta name="description" content="Css aspect ratio to have images of same height">
        <!-- include bootstrap -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
        <style type="text/css">
            /* item styles */
            .item-image {
                position: relative;
                overflow: hidden;
                padding-bottom: 50%;
            }
            .item-image img {
                position: absolute;
                top: 0;
                bottom: 0;
                left: 0;
                right: 0;
                margin: auto;
            }
            .item-content {
                padding: 15px;
                background: #72cffa;
            }
            .item-text {
                position: relative;
                overflow: hidden;
                height: 100px;
            }

            /* centering styles for jsbin */
            html,
            body {
                width: 100%;
                height: 100%;
            }
            html {
                display: table;
            }
            body {
                display: table-cell;
                vertical-align: middle;
            }

        </style>
    </head>
    <body>

        <div class="container" id="appendHere">
<!--            <div class="row">

                <div class="col-xs-12 col-sm-4">
                    <div class="item-image">
                        <img src="http://placehold.it/800x400" class="img-responsive">
                    </div>
                    <div class="item-content"><div class="item-text">
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        </div></div>
                </div>

                <div class="col-xs-12 col-sm-4">
                    <div class="item-image">
                        <img src="http://placehold.it/800x500" class="img-responsive">
                    </div>
                    <div class="item-content">
                        <div class="item-text">
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit.
                        </div>
                    </div>
                </div>

                <div class="col-xs-12 col-sm-4">
                    <div class="item-image">
                        <img src="http://placehold.it/850x600" class="img-responsive">
                    </div>
                    <div class="item-content">
                        <div class="item-text">
                            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas nec velit nulla. Cras finibus mollis dolor, ac rhoncus diam. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin gravida iaculis orci eget malesuada. Morbi pulvinar, purus sit amet porta laoreet, quam orci fermentum leo, in interdum ligula elit id magna. Nam justo massa, ultrices eget dictum vitae, porttitor eget eros. 
                        </div>
                    </div>
                </div>

            </div>-->
        </div>

        <script src="../js/jquery.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="../js/bootstrap.min.js"></script>

        <script type="text/javascript" >
            var CURRENT_PAGE = 1;
            $(document).ready(function () {
                getPage();
                setPage();
            });
            $("#next").on("click", function (event) {
                CURRENT_PAGE = CURRENT_PAGE + 1;
                getPage();
            });
            $("#prev").on("click", function (event) {
                if (CURRENT_PAGE > 1) {
                    CURRENT_PAGE = CURRENT_PAGE - 1;
                }
                if (CURRENT_PAGE > 0) {
                    getPage();
                }
            });

            function setPage() {
                var min = Infinity;
                $els = $('.portfolio-item');
                $els.each(function () {
                    min = Math.min($(this).height());
                });
                //alert(min);
                $els.parent('div').css({
                    'height': min,
                    'overflow': 'hidden'
                });
            }

            function getPage() {
                $.ajax({
                    url: "index/" + CURRENT_PAGE,
//                    cache: false,
                    success: function (feedList) {
                        var html = '';
                        for (var i = 0; i < feedList.length; i++) {
                            var data = feedList[i];
                            console.log(data);
                            if (html === '') {
                                html = '<div class="row">';
                            }

                            //Get a one line description...
                            var desc = '';
                            try {
                                desc = $(data.description).text().split("\n");
                                if (desc.length > 0) {
                                    desc = desc[0];
                                } else {
                                    desc = $(data.description).text();
                                }


                            } catch (err) {
                                desc = $('<span>' + data.description + '<\span>').text().split("\n");
                                if (desc.length > 0) {
                                    desc = desc[0];
                                } else {
                                    desc = $('<span>' + data.description + '<\span>').text();
                                }
                                console.log("Has error!!!");
                                console.log(desc);
                            }
                            console.log("Description:" + desc);

                            var html = html + '<div class="col-xs-12 col-sm-4">'
                                    + '<div class="item-image"><a href="#">'
                                    +'<img class="img-responsive" src="' + data.image + '" alt="" ></a></div>'
                                    + '<div class="item-content"><div class="item-text"><h3><a href="#">' + data.title + '</a></h3>'
                                    + '<p>' + desc + '</p></div></div></div>';
                            if (i > 0 && ((i + 1) % 3 == 0) && i < feedList.length) {
                                html = html + '</div><div class="row">';
                            }
                            //$( "div.demo-container" ).text()
                            //string s = '<div id="myDiv"></div>'
                            //var htmlObject = $(s); // jquery call
                            if (i == feedList.size) {
                                html = html + '</div>';
                            }

                        }
                        $("#appendHere").html(html);
                    }
                });
            }

        </script>


    </body>
</html>
