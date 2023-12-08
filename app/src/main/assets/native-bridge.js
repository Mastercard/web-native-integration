console.log("loading post-messaging.js")

const click2payInstance = new Click2Pay();

function sendMessageToNative(platform, json, methodName){
  var formattedJSON = JSON.stringify(json, 0, 2);
  switch(platform) {
    case "ios":
       window.webkit.messageHandlers.jsMessageHandler.postMessage(formattedJSON);
       break;
    case "android":
      JSBridge.showMessageInNative(formattedJSON, methodName);
      break;
  }
}

function initSdk(platform, apiRequest) {
	console.log('inside init:')
	console.log("init req: "+ apiRequest);

	let initRequest = JSON.parse(apiRequest);
  	let cardBrands = initRequest.cardBrands;
  	let srcDpaId = initRequest.srcDpaId;
  	let dpaTransactionOptions = initRequest.dpaTransactionOptions;
  	let dpaData = initRequest.dpaData;

  	const sampleInitParams = {srcDpaId, cardBrands, dpaTransactionOptions, dpaData};

	let promise = new Promise(
		function(resolve, reject) {
			click2payInstance.init(initRequest).then(resolve)
		});

	promise.then(
		value => {
			var formattedResponses = JSON.stringify(value, null, 2);
			console.groupCollapsed('Init API response:')
			console.log(formattedResponses)
			console.groupEnd();

			sendMessageToNative(platform, value, "init");
		},
		error => {
		sendMessageToNative(platform, error, "init");
		console.log('Init API rejected '+ error)
		});
}

function getCards(platform, apiRequest) {
	console.log('inside getCards:')

	click2payInstance.getCards().then(
		value => {
			var formattedResponses = JSON.stringify(value, null, 2);
			console.groupCollapsed('GetCards API response:')
			console.log(formattedResponses)
			console.groupEnd();
			sendMessageToNative(platform, value, "getCards");
		}).catch(error =>{
                    sendMessageToNative(platform, error, "getCards");
                    console.log('GetCards API rejected '+ error)
                    });
}

function encryptCard(platform, apiRequest) {
	console.log('inside encryptCard:')

	let cardData = JSON.parse(apiRequest);

	click2payInstance.encryptCard(cardData).then(
		value => {
			var formattedResponses = JSON.stringify(value, null, 2);
			console.groupCollapsed('EncryptCard API response:')
			console.log(formattedResponses)
			console.groupEnd();
			sendMessageToNative(platform, value, "encryptCard");
		}).catch(error =>{
                 sendMessageToNative(platform, error, "encryptCard");
		         console.log('EncryptCard API rejected '+ error)
                 });
}

function idLookup(platform, apiRequest) {
	console.log('inside idLookup:')

	let request = JSON.parse(apiRequest);

	click2payInstance.idLookup(request).then(
		value => {
			var formattedResponses = JSON.stringify(value, null, 2);
			console.groupCollapsed('IdLookup API response:')
			console.log(formattedResponses)
			console.groupEnd();
			sendMessageToNative(platform, value, "idLookup");
		}).catch(error =>{
        sendMessageToNative(platform, error, "idLookup");
        console.log('IdLookup API rejected '+ error)
        });
}

let responseObject = null
function initiateValidation(platform, apiRequest) {
  console.log('inside initiateValidation:')

  let request = JSON.parse(apiRequest);
	console.log("the request is " + request.requestedValidationChannelId)

	let initiateValidation = null
  if (responseObject === null) {
    console.log("calling initiate validation for the first time")
    initiateValidation = click2payInstance.initiateValidation()
  } else {
    console.log("nth initiate validation call")

    let channelFound = false
    for (let i = 0; i < responseObject.supportedValidationChannels.length; i++) {
      if (responseObject.supportedValidationChannels[i].identityType === request.requestedValidationChannelId) {
        request.requestedValidationChannelId = responseObject.supportedValidationChannels[i].validationChannelId
        channelFound = true
      }
    }

    initiateValidation = channelFound? click2payInstance.initiateValidation(request): click2payInstance.initiateValidation()
  }

  initiateValidation.then(
    value => {
      let formattedResponses = JSON.stringify(value, null, 2);
      console.groupCollapsed('InitiateValidation API response:')
      console.log(formattedResponses)
      console.groupEnd();
      sendMessageToNative(platform, value, "initiateValidation");
      responseObject = value
      console.log(responseObject)
    }).catch(error =>{
    sendMessageToNative(platform, error, "initiateValidation");
    console.log('InitiateValidation API rejected '+ error)
        });
}

function validate(platform, apiRequest) {
	console.log('inside validate:')
	console.log('Validate request: '+apiRequest)

	let request = JSON.parse(apiRequest);

	click2payInstance.validate(request).then(
		value => {
			var formattedResponses = JSON.stringify(value, null, 2);
			console.groupCollapsed('Validate API response:')
			console.log(formattedResponses)
			console.groupEnd();
			sendMessageToNative(platform, value, "validate");
		}).catch(error =>{
		sendMessageToNative(platform, error, "errorValidate");
        console.log('Validate API rejected '+ error)
		});
}

function checkoutWithNewCard(platform, apiRequest, actionSheetMode) {
    console.log('inside checkoutWithNewCard:');
    let checkoutRequest = JSON.parse(apiRequest);
    var iframe = document.getElementById("dcfLaunch");
    checkoutRequest.windowRef = iframe.contentWindow;

  if(actionSheetMode == true) {
     iframe.classList.add('actionSheet')
     function addClass () {
       iframe.classList.add('loaded')
     }
     iframe.addEventListener('load', addClass)
   } else {
       iframe.classList.remove('actionSheet')
   }

      click2payInstance.checkoutWithNewCard(checkoutRequest).then(
        value => {

         	if(actionSheetMode == true) {
           iframe.removeEventListener('load', addClass)
           iframe.classList.remove('loaded')
          }

            var formattedResponses = JSON.stringify(value, null, 2);
            console.groupCollapsed('Checkout API response:')
            console.log(formattedResponses)
            console.groupEnd();
            console.log("before dcf launch frame");
            var frame = document.getElementById("dcfLaunch");
            console.log("after dcf launch frame and before blank");
            frame.src = "about:blank"
            console.log("after blank");
            sendMessageToNative(platform, value, "checkoutWithNewCard");
        }).catch(error =>{
         sendMessageToNative(platform, error, "checkoutWithNewCard");
         console.log('Checkout API rejected '+ error)
         });
}

function checkoutWithCard(platform, apiRequest, actionSheetMode) {
	console.log('inside checkoutWithCard:');
  console.log('CheckoutRequest: '+ apiRequest);
	let checkoutRequest = JSON.parse(apiRequest);

  var iframe = document.getElementById("dcfLaunch");
	checkoutRequest.windowRef = iframe.contentWindow;

  if(actionSheetMode == true) {
    iframe.classList.add('actionSheet')
    function addClass () {
      iframe.classList.add('loaded')
    }
    iframe.addEventListener('load', addClass)
  } else {
      iframe.classList.remove('actionSheet')
  }

  click2payInstance.checkoutWithCard(checkoutRequest).then(
  		value => {
  		if(actionSheetMode == true) {
       iframe.removeEventListener('load', addClass)
       iframe.classList.remove('loaded')
     }
  			var formattedResponses = JSON.stringify(value, null, 2);
  			console.groupCollapsed('Checkout API response:')
  			console.log(formattedResponses)
  			console.groupEnd();

  			var frame = document.getElementById("dcfLaunch");
  			frame.src = "about:blank"
  			console.log("after blank");
    		sendMessageToNative(platform, value, "checkoutWithCard");
  		},
  		error => console.log('Checkout API rejected '+ error));
}