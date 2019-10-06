import React from 'react'
import Popup from "reactjs-popup"

export default class HelpPopout extends React.Component {

    render() {
        return (
            <Popup trigger={<button title="Game help" id='help-button' type="button">?</button>}
                modal
                closeOnDocumentClick>
                <span>
                    <h2>Help</h2>
                    <h4>This is an online version of the card game <i>Magic the Gathering</i>. If you have never played before, please refer to the <a href="https://magic.wizards.com/en/magic-gameplay">
                        <i>official game rules</i></a>.</h4>
                    <h5>The following are rules specific to this version:</h5>
                    <ul>
                        <li>Mana needs to be tapped before to click the card to play</li>
                        <li>Turn phases are on the right, the light green is the current phase and the little circle next to it indicate the player with priority</li>
                        <li>The underlined player is the player that has the current turn</li>
                        <li>The status bar at the bottom tells you what to do</li>
                        <li>If a card/player is clicked and no action can be done at the moment probably nothing will happen</li>
                    </ul>
                    <h5>The following are availible navigation shortcuts:</h5>
                    <ul>
                        <li>SPACE - continue</li>
                    </ul>
                </span>
            </Popup>
        );
    }
}