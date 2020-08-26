var webAuth;

function getAuth0DomainAndClientId() {
    const domain = $("input[name='auth0_domain']").val() || "";
    const clientId = $("input[name='auth0_client_id']").val() || "";
    return {
        auth0Domain: domain,
        auth0ClientId:clientId
    };
}

function initAuth0() {
    logUtil('initAuth0: initializing Auth0..');
    const auth0Config = getAuth0DomainAndClientId();
    if (auth0Config.auth0Domain !== "" && auth0Config.auth0ClientId !== "") {
        webAuth = new auth0.WebAuth({
            domain: auth0Config.auth0Domain,
            clientID: auth0Config.auth0ClientId
        });
        logUtil('initAuth0: successful auth0 initialization.');
        return true;
    }
    return false;
}

function fbSignIn() {
    if (initAuth0()) {
        showSpinner();
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
            hideSpinner();
            logUtil("fbAuth: Auth0 exception:" + e.message);
        }
    }
}

function appleSignIn() {
    if (initAuth0()) {
        showSpinner();
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
            hideSpinner();
            logUtil("appleSignIn: Auth0 exception:" + e.message);
        }
    }
}

function googleSignIn() {
    if (initAuth0()) {
        showSpinner();
        try {
            logUtil('googleSignIn: initializing Auth0..');

            // Running auth0 google auth flow...
            webAuth.authorize({
                connection: 'google-oauth2',
                responseType: 'token',
                redirectUri: window.location.origin + window.location.pathname
            });
            logUtil('googleSignIn: successful auth0 initialization.');
        } catch (e) {
            hideSpinner();
            logUtil("googleSignIn: Auth0 exception:" + e.message);
        }
    }
}

function logUtil(message) {
    console.log(message);
}

function loginWithFaceBook() {
    if (window.location.hash) {
        try {
            logUtil("attempting to parse hash with token from social login..");
            if (initAuth0()) {
                showSpinner();
                webAuth.parseHash(window.location.hash, function (err, authResult) {
                    if (err) {
                        hideSpinner();
                        logUtil("social login: err from parse hash(maybe just bad login credentials): " + JSON.stringify(err));
                        logUtil("login fail when login with social media");
                        var errorMessage = "The email address and/or password you provided does not match a valid Pimsleur account.";
                        showFailAlert(errorMessage);
                    } else {
                        logUtil("social login: successfully parsed url hash. Attempting to get user info..");
                        webAuth.client.userInfo(authResult.accessToken, function (err, user) {
                            hideSpinner();
                            if (err) {
                                logUtil("social login: err from client.userInfo : " + JSON.stringify(err));
                                var errorMessage = "Invalid Login, " + "Autentication error.";
                                showFailAlert(errorMessage);
                            } else {
                                logUtil("social login: successfully retrieved user info: " + JSON.stringify(user));
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
            logUtil("loginWithSocialMedia: exception: " + e.message);
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
        fbSignIn();
    });
    document.getElementById("apple-login").addEventListener("click", function () {
        appleSignIn();
    });
    document.getElementById("google-login").addEventListener("click", function () {
        googleSignIn();
    });

    storeFieldsFromAlexa();
    loginWithFaceBook();

    initModalAndCheckError();
    initLoginFormAction();
});


