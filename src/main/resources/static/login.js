const AUTH0_DOMAIN = "mg2-ss-prod.auth0.com";
const AUTH0_CLIENT_ID = "OYONqZ4zLlVruKMT9FhCtlV1idp7wrPJ";

var webAuth;

function initAuth0() {
    logUtil('initAuth0: initializing Auth0..');
    try {
        webAuth = new auth0.WebAuth({
            domain: AUTH0_DOMAIN,
            clientID: AUTH0_CLIENT_ID
        });
        logUtil('initAuth0: successful auth0 initialization.');
        console.log(webAuth);
        return true;
    } catch (e) {
        logUtil("initAuth0: Auth0 exception:" + e.message);
    }
    return false;
}

function fbAuth() {
    showSpinner();

    if (initAuth0()) {

        try {
            logUtil('fbAuth: initializing Auth0..');

            // Running auth0 Facebook auth flow...
            webAuth.authorize({
                connection: 'facebook',
                responseType: 'token',
                redirectUri: window.location.origin + window.location.pathname
            });
            logUtil('fbAuth: successful auth0 initialization.');
        } catch (e) {
            logUtil("fbAuth: Auth0 exception:" + e.message);
        }
    }
}

function appleSignIn() {
    showSpinner();
    if (initAuth0()) {
        try {
            logUtil('appleSignIn: initializing Auth0..');

            // Running auth0 Apple auth flow...
            webAuth.authorize({
                connection: 'apple',
                responseType: 'token',
                redirectUri: window.location.origin + window.location.pathname
            });
            logUtil('appleSignIn: successful auth0 initialization.');
        } catch (e) {
            logUtil("appleSignIn: Auth0 exception:" + e.message);
        }
    }
}

function logUtil(message) {
    console.log(message);
}

function loginWithFaceBook() {
    if (window.location.hash) {
        showSpinner();

        try {
            logUtil("fbAuth: attempting to parse hash with token from social login..");
            if (initAuth0()) {
                webAuth.parseHash(window.location.hash, function (err, authResult) {
                    if (err) {
                        hideSpinner();
                        logUtil("fbAuth: err from parse hash(maybe just bad login credentials): " + JSON.stringify(err));
                        logUtil("login fail when login with Facebook");
                        var errorMessage = "The email address and/or password you provided to Facebook does not match a valid Pimsleur account.";
                        showFailAlert(errorMessage);
                    } else {
                        logUtil("fbAuth: successfully parsed url hash. Attempting to get user info..");
                        webAuth.client.userInfo(authResult.accessToken, function (err, user) {
                            hideSpinner();
                            if (err) {
                                logUtil("fbAuth: err from client.userInfo : " + JSON.stringify(err));
                                var errorMessage = "Invalid Login, " + "Autentication error.";
                                showFailAlert(errorMessage);
                            } else {
                                logUtil("fbAuth: successfully retrieved user info: " + JSON.stringify(user));
                                var fullRedirectUrl = window.localStorage.getItem("redirect_uri") + "#"
                                    + "state=" + window.localStorage.getItem("state") + "&"
                                    + "access_token=" + user.sub + "&"
                                    + "token_type=" + "Bear";

                                window.location.href = fullRedirectUrl;
                            }
                        });
                    }
                });
            }
        }
        catch (e) {
            logUtil("loginWithFaceBook: exception: " + e.message);
        }
    }
}

function storeFieldsFromAlexa() {
    var $stateElement = $("input[name='state']");
    if ($stateElement.val() != undefined && $stateElement.val() != null && $stateElement.val() != "") {
        window.localStorage.setItem("state", $stateElement.val());
    } else {
        $stateElement.val(window.localStorage.getItem("state"));
    }

    var $redirectUrlElement = $("input[name='redirect_uri']");
    if ($redirectUrlElement.val() != undefined && $redirectUrlElement.val() != null && $redirectUrlElement.val() != "") {
        window.localStorage.setItem("redirect_uri", $redirectUrlElement.val());
    } else {
        $redirectUrlElement.val(window.localStorage.getItem("redirect_uri"));
    }
}

var showFailAlert = function (errorMessage) {
    $("#alert-modal").removeClass("off");
    $("#alert-txt").text(errorMessage);
};

var showSpinner = function () {
    $("#spinner").removeClass("off");
};

var hideSpinner = function () {
    $("#spinner").addClass("off");
};

var initModalAndCheckError = function () {
    $('#alert-btn').on("click", function () {
        $("#alert-modal").addClass("off");
    });

    var url = new URL(window.location.href);
    if (url.searchParams.get("loginStatus") == "fail") {
        logUtil("login fail when login with normal Pimsleur account");
        var errorMessage = "Your username or password doesn't match";
        showFailAlert(errorMessage);
    }
};

function initLoginFormAction() {
    $("#regular-login").on("click", function () {
        var redirectDestination = "";

        $.ajax({
            method: "POST",
            url: "login",
            data: {
                state: $("input[name='state']").val(),
                redirect_uri: $("input[name='redirect_uri']").val(),
                email: $("input[name='email']").val(),
                password: $("input[name='password']").val()
            }
        }).done(function(data, status, jqXHR) {
            redirectDestination = data;
            window.location.href = redirectDestination;
        }).fail(function (data) {
            logUtil(data);
        });
    });
}

$(document).ready(function () {
    document.getElementById("fb-login").addEventListener("click", function () {
        fbAuth();
    });
    
    document.getElementById("apple-login").addEventListener("click", function () {
        appleSignIn();
    });

    storeFieldsFromAlexa();
    loginWithFaceBook();

    initModalAndCheckError();
    initLoginFormAction();
});


