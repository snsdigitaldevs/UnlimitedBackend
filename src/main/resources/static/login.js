// function _cleanupTmpStorage() {
//     localStorage.clear();
// }


const AUTH0_DOMAIN = "mg2-ss-prod.auth0.com";
const AUTH0_CLIENT_ID = "OYONqZ4zLlVruKMT9FhCtlV1idp7wrPJ";

var webAuth;

function initAuth0ForFBLogin(){
    logUtil('initAuth0ForFBLogin: initializing Auth0..');
    try {
        webAuth = new auth0.WebAuth({
            domain: AUTH0_DOMAIN,
            clientID: AUTH0_CLIENT_ID
        });
        logUtil('initAuth0ForFBLogin: successful auth0 initialization.');
        console.log(webAuth);
        return true;
    } catch (e) {
        logUtil("initAuth0ForFBLogin: Auth0 exception:" + e.message);
    }
    return false;
}

function fbAuth() {
    // Facebook auth
    //
    // _cleanupTmpStorage();

    // showSpinner("Login with Facebook..");

    if (initAuth0ForFBLogin()) {

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

function logUtil(message) {
    console.log(message);
}

function doOnPageLoad() {
    // document.getElementById('edt-version').innerHTML=Config.edtVersion();

    // logUtil("edt login version:"+Config.edtVersion());

    // should be a redirect from auth for social login
    if (window.location.hash) {
        //todo: show loader image
        // showSpinner("Login with Facebook..");

        try {
            logUtil("fbAuth: attempting to parse hash with token from social login..");
            if (initAuth0ForFBLogin()) {
                webAuth.parseHash(window.location.hash, function (err, authResult) {
                    if (err) {
                        logUtil("fbAuth: err from parse hash(maybe just bad login credentials): " + JSON.stringify(err));
                        //todo: show alert
                        // showAlert("Invalid Login", "The email address and/or password you provided to Facebook does not match a valid Pimsleur account.");

                    } else {
                        logUtil("fbAuth: successfully parsed url hash. Attempting to get user info..");
                        webAuth.client.userInfo(authResult.accessToken, function (err, user) {

                            if (err) {
                                logUtil("fbAuth: err from client.userInfo : " + JSON.stringify(err));
                                //todo: show alert
                                // showAlert("Invalid Login", "Autentication error.");
                            } else {
                                logUtil("fbAuth: successfully retrieved user info: " + JSON.stringify(user));
                                const fbUserId = user.sub;
                                //todo: redirect to alexa service.
                                var fullRedirectUrl = window.localStorage.getItem("redirect_uri") + "#"
                                        + "state=" + window.localStorage.getItem("state") + "&"
                                        + "access_token=" + user.sub + "&"
                                        + "token_type=" + "Bear";

                                logUtil("fbAuth: clearing local storage");
                                // _cleanupTmpStorage();
                                window.localStorage.clear();
                            }
                        });
                    }
                });
            }
        }
        catch (e) {
            logUtil("doOnPageLoad: exception: " + e.message);
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

$( document ).ready(function() {
    document.getElementById("fb-login").addEventListener("click", function () {
        fbAuth();
    });

    storeFieldsFromAlexa();
    doOnPageLoad();
});


