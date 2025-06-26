import React from "react";
import "./NavBar.css";
import { NotificationsNone, Language } from "@material-ui/icons";
import AccountMenu from "../Accountmenu/AccountMenu";

export default function NavBar() {
    return (
        <div className="topbar">
            <div className="topbarWrapper">
                <div className="topRight">
                    <div className="topbarIconContainer">
                        <NotificationsNone />
                        <span className="topIconBadge">2</span>
                    </div>
                    <div className="topbarIconContainer">
                        <Language />
                        <span className="topIconBadge">2</span>
                    </div>
                    <AccountMenu />
                </div>
            </div>
        </div>
    );
}