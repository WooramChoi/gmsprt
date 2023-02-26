package net.adonika.gmsprt.security.model.impl;

import net.adonika.gmsprt.security.model.OAuth2UserInfo;

import java.util.Map;

public class GithubOAuth2UserInfo extends OAuth2UserInfo {

    /**
     * {
     *   "login": "octocat",
     *   "id": 1,
     *   "node_id": "MDQ6VXNlcjE=",
     *   "avatar_url": "https://github.com/images/error/octocat_happy.gif",
     *   "gravatar_id": "",
     *   "url": "https://api.github.com/users/octocat",
     *   "html_url": "https://github.com/octocat",
     *   "followers_url": "https://api.github.com/users/octocat/followers",
     *   "following_url": "https://api.github.com/users/octocat/following{/other_user}",
     *   "gists_url": "https://api.github.com/users/octocat/gists{/gist_id}",
     *   "starred_url": "https://api.github.com/users/octocat/starred{/owner}{/repo}",
     *   "subscriptions_url": "https://api.github.com/users/octocat/subscriptions",
     *   "organizations_url": "https://api.github.com/users/octocat/orgs",
     *   "repos_url": "https://api.github.com/users/octocat/repos",
     *   "events_url": "https://api.github.com/users/octocat/events{/privacy}",
     *   "received_events_url": "https://api.github.com/users/octocat/received_events",
     *   "type": "User",
     *   "site_admin": false,
     *   "name": "monalisa octocat",
     *   "company": "GitHub",
     *   "blog": "https://github.com/blog",
     *   "location": "San Francisco",
     *   "email": "octocat@github.com",
     *   "hireable": false,
     *   "bio": "There once was...",
     *   "twitter_username": "monatheoctocat",
     *   "public_repos": 2,
     *   "public_gists": 1,
     *   "followers": 20,
     *   "following": 0,
     *   "created_at": "2008-01-14T04:33:35Z",
     *   "updated_at": "2008-01-14T04:33:35Z"
     * }
     * @param attributes
     */

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getSid() {
        return attributes.get("id").toString();
    }

    @Override
    public String getUid() {
        return attributes.get("login").toString();
    }

    @Override
    public String getName() {
        return attributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getUrlPicture() {
        return attributes.get("avatar_url").toString();
    }
}
