import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import './App.css';
import NavBar from "./component/NavBar";
import Home from "./view/Home";
import Login from "./view/Login";
import Register from "./view/Register";
import Client from "./view/Client";
import Order from "./view/Order";
import PrivateRoute from "./component/PrivateRoute";

function App() {

    return (
    <div className="App">
        <Router>
            <NavBar/>
            <Switch>
                <PrivateRoute component={Home} path="/" exact />
                <PrivateRoute component={Client} path="/client" exact />
                <PrivateRoute component={Order} path="/order" exact />
                <Route component={Login} path="/login" exact />
                <Route component={Register} path="/register" exact />
            </Switch>
        </Router>
    </div>
  );
}

export default App;
