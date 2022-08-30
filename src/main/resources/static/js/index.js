var ApiClient = (function () {

	const _CHECK_STOCKS_PATH = "/checkStocks";
	const _TIMEFRAME_VALUES_PATH = "/getTimeframes";
	const _TIMEINTERVAL_VALUES_PATH = "/getTimeIntervals";

	async function _retrievingData(path, parameters) {
		let response = await fetch(_PATH + parameters);
		let data = await response.json();
		return data;
	}


	function _fillTimeframeSelect() {
		values = JSON.stringify(_retrievingData(_TIMEFRAME_VALUES_PATH, ""));
		console.log(values);
	}


	function _fillTimeIntervalSelect() {
		values = JSON.stringify(_retrievingData(_TIMEINTERVAL_VALUES_PATH, ""));
		console.log(values);
	}

	function init() {
		_fillTimeframeSelect();
		_fillTimeIntervalSelect();
	}

	return {
		init: init
	}

})();