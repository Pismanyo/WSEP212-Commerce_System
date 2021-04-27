import React from "react";
import StaticUserInfo from "../API/StaticUserInfo";
/***
 * Use Case : 4.1
 */

class AddProduct extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            functionName: 'addProductsToStore',
            username: StaticUserInfo.getUsername(),
            name: '',
            storeId: StaticUserInfo.getStoreId(),
            price: '',
            categories: '',
            keywords: '',
            amount: '',
        };
    }

    handleClick(e) {
        // access input values in the state
        console.log(this.state)
         e.preventDefault();
    }

    handleInputChange = (e, name) => {
        this.setState({
            [name]: e.target.value
        })
    }

    render() {
        return (
            <form>
                <h1>Add Product Page </h1>
                <div> <label> Username : <input readOnly value = {this.state.username} className = "username" type = "text" onChange = {(e) => this.handleInputChange(e, 'username')}/> </label> </div>
                <div> <label> Product Name : <input className = "name" type = "text" onChange = {(e) => this.handleInputChange(e, 'name')}/> </label> </div>
                <div> <label> Store Id : <input readOnly value = {this.state.storeId} className = "storeId" type = "text" onChange = {(e) => this.handleInputChange(e, 'storeId')}/> </label> </div>
                <div> <label> Price : <input className = "price" type = "text" onChange = {(e) => this.handleInputChange(e, 'price')}/> </label> </div>
                <div> <label> Categories* : <input className = "categories" type = "text" placeholder='c1,c2,...' onChange = {(e) => this.handleInputChange(e, 'categories')}/> </label> </div>
                <div> <label> Keywords* : <input className = "keywords" type = "text" placeholder='k1,k2,...' onChange = {(e) => this.handleInputChange(e, 'keywords')}/> </label> </div>
                <div> <label> Amount : <input className = "amount" type = "text" onChange = {(e) => this.handleInputChange(e, 'amount')}/> </label> </div>
                <button type = "button" onClick = {(e) => this.handleClick(e)}> Add Product </button>
            </form>
        )
    }
}

export default AddProduct;