/*global cordova, module*/

module.exports = {
    greet: function (mac, key, bluetoothID, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "greet", [mac, key, bluetoothID]);
	},
	connect: function(mac, key, bluetoothID, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "connect", [mac, key, bluetoothID]);
	},
	openDoor: function(successCallback, errorCallback) {
		cordova.exec(successCallback, errorCallback, "Hello", "openDoor", []);
    },
	closeDoor: function(successCallback, errorCallback) {
	cordova.exec(successCallback, errorCallback, "Hello", "closeDoor", []);
	},
    callCar: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "callCar", []);
    },
    resetBle: function(successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "Hello", "resetBle", []);
    }
};
