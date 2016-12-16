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
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Css aspect ratio to have images of same height</title>
        <meta name="description" content="Css aspect ratio to have images of same height">
        <!-- include bootstrap -->
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
        <link href="../css/3-col-portfolio.css" rel="stylesheet"/>
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

        <!-- Navigation -->
        <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
            <div class="container">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" href="#">Start Bootstrap</a>
                </div>
                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav">
                        <li>
                            <a href="#">About</a>
                        </li>
                        <li>
                            <a href="#">Services</a>
                        </li>
                        <li>
                            <a href="#">Contact</a>
                        </li>
                    </ul>
                </div>
                <!-- /.navbar-collapse -->
            </div>
            <!-- /.container -->
        </nav>
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
        <hr>

        <!-- Pagination -->
        <div class="row text-center">
            <div class="col-lg-12">
                <ul class="pagination">
                    <li>
                        <a href="#">&laquo;</a>
                    </li>
                    <li>
                        <a href="#" id="prev"><<</a>
                    </li>
                    <li>
                        <a href="#" id="next">>></a>
                    </li>
                    <li>
                        <a href="#">&raquo;</a>
                    </li>
                </ul>
            </div>
        </div>
        <!-- /.row -->

        <hr>

        <!-- Footer -->
        <footer>
            <div class="row">
                <div class="col-lg-12">
                    <p>Copyright &copy; Your Website 2014</p>
                </div>
            </div>
            <!-- /.row -->
        </footer>


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
                            var desc = $('<span>' + data.description+ '<\span>').text();
//                            try {
//                                desc = $('<span>' + data.description+ '<\span>').text().split("\n");
//                                if (desc.length > 0) {
//                                    desc = desc[0];
//                                } else {
//                                    desc = $(data.description).text();
//                                }
//
//
//                            } catch (err) {
//                                desc = $('<span>' + data.description + '<\span>').text().split("\n");
//                                if (desc.length > 0) {
//                                    desc = desc[0];
//                                } else {
//                                    desc = $('<span>' + data.description + '<\span>').text();
//                                }
//                                console.log("Has error!!!");
//                                console.log(desc);
//                            }
                            console.log("Description:" + desc);

                            var html = html + '<div class="col-xs-12 col-sm-4">'
                                    + '<div class="item-image"><a href="#">'
                                    + '<img class="img-responsive" src="' + data.image + '" width="100%" ></a></div>'
                                    + '<div class=""><div class=""><h3><a href="'+data.feedurl+'">' + data.title + '</a></h3>'
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
