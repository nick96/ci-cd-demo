import "./App.css";

import React from "react";

const REST_URI_BASE = process.env.REACT_APP_REST_URI_BASE;

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = { conversion: "toKg", value: 0.0, converted: undefined };
  }

  handleInputChange = event => {
    this.setState({ value: event.target.value });
  };

  handleChoiceChange = event => {
    this.setState({ conversion: event.target.value });
  };

  onSubmit = event => {
    event.preventDefault();
    let url = `${REST_URI_BASE}/conversion?`;
    switch (this.state.conversion) {
      case "toKg": {
        url += `from=lb&to=kg&value=${this.state.value}`;
        break;
      }
      case "toLb": {
        url += `from=kg&to=lb&value=${this.state.value}`;
        break;
      }
      default:
        console.error(`Unhandled conversion ${this.state.conversion}`);
    }
    fetch(url)
      .then(resp => {
        resp.json().then(json => this.setState({ converted: json.value }));
      })
      .catch(err => console.error(`Error: ${err}`));
  };

  render() {
    return (
      <div className="App">
        <form onSubmit={this.onSubmit}>
          <div className="value-input">
            <label>
              Value:
              <input
                type="text"
                name="value"
                value={this.state.value}
                onChange={this.handleInputChange}
              />
            </label>
          </div>
          <div className="options">
            <input
              type="radio"
              name="conversion"
              value="toKg"
              onChange={this.handleChoiceChange}
              checked={this.state.conversion === "toKg"}
            />
            To Kg
            <input
              type="radio"
              name="conversion"
              value="toLb"
              onChange={this.handleChoiceChange}
              checked={this.state.conversion === "toLb"}
            />
            To Lb
          </div>

          <input type="submit" value="submit" />
        </form>

        <div>
          {this.state.converted}
        </div>
      </div>
    );
  }
}

export default App;
