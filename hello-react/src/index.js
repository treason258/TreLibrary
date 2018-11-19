// import React from 'react';
// import ReactDOM from 'react-dom';
// import './index.css';
// import App from './App';
// import * as serviceWorker from './serviceWorker';
//
// ReactDOM.render(<App/>, document.getElementById('root'));
//
// // If you want your app to work offline and load faster, you can change
// // unregister() to register() below. Note this comes with some pitfalls.
// // Learn more about service workers: http://bit.ly/CRA-PWA
// serviceWorker.unregister();


import React from 'react';
import ReactDOM from 'react-dom';
import CommentApp from './comment/CommentApp.js';
import Header from './test/Header.js';
import All from './test/All.js';
import Clock from './test/Clock.js';
import AutoFocusInput from "./test/AutoFocusInput";
import Context from "./test/Context";

ReactDOM.render(<CommentApp/>, document.getElementById('root'));