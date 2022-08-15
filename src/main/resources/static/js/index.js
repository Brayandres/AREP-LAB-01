var ApiClient = (function () {

	const PATH = "/checkStocks";

	async function _retrievingData(parameters) {
		let response = await fetch(PATH + parameters);
		let data = await response.json();
		console.log("Response: "+JSON.stringify(data));
	}

	const PARAMS = "?function=INTRA_DAY" + "&symbol=IBM" + "&interval=FIFTEEN_MIN";

	function init() {
		console.log("------- IN INIT()");
		//const requestBody = prepareData();
		const parameters = PARAMS;
		console.log("Parameters: "+parameters);
		_retrievingData(parameters);
		console.log("------- END INIT()");
	}

	return {
		init: init
	}

})();