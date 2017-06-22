'use strict';

let React = require('react-native');

let {
  Component,
} = React;

let ERROR = 'Not available in iOS - use Geolocation Polyfill.';

class PlayServicesLocation extends Component {
  constructor (props) {
    super(props);
    console.log(ERROR);
  }

  getCurrentLocation () { console.error(ERROR) }
}

module.exports = PlayServicesLocation;
