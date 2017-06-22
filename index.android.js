/**
 * @providesModule PlayServicesLocation
 */

'use strict';

/**
 * This Module is a Quick and Dirty Hack to return Location from Google Play Services Location for Android
 */

const { NativeModules } = require('react-native');
const PlayServicesLocation = NativeModules.PlayServicesLocation;
const getCurrentLocation = (timeout = 5000, maxAge = 30000) => PlayServicesLocation.getCurrentLocation(timeout, maxAge)

module.exports = {
  ...PlayServicesLocation,
  getCurrentLocation,
};
