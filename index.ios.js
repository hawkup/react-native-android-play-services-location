'use strict';

let React = require('react-native');

let {
  Component,
} = React;

let ERROR = 'Not available in iOS - use Geolocation Polyfill.';

class RNLastKnownLocation extends Component {
  constructor (props) {
    super(props);
    console.log(ERROR);
  }

  getCurrentLocation () { console.error(ERROR) }
}

module.exports = RNLastKnownLocation;
