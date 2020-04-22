package de.embl.cba.plateviewer.github;

public class GitHubFileCommitter
{
	private String userName;
	private String repository;
	private String accessToken;
	private String path;

	public GitHubFileCommitter( String userName, String repository, String accessToken, String path )
	{
		this.userName = userName;
		this.repository = repository;
		this.accessToken = accessToken;
		this.path = path;
	}

	public void commitFile()
	{
		final GitHubFileCommit fileCommit = new GitHubFileCommit();
		String url = createFileCommitApiUrl( path );
		final String requestMethod = "POST";
		final String content = fileCommit.toString();

		RESTCaller.call( url, requestMethod, content, accessToken );

//		final HttpResponse< String  > response =
//				Unirest.post( url )
//						.header( "A", "a" )
//						.header( "accept", "application/json" )
//						.basicAuth( userName, accessToken )
//						.body( issueJson )
//				.asString();
//
//		System.out.println( response.getBody() );
	}

	public String createFileCommitApiUrl( String path )
	{
		String url = repository.replace( "github.com", "api.github.com/repos" );
		if ( ! url.endsWith( "/" ) ) url += "/";
		if ( ! path.startsWith( "/" ) ) path = "/" + path;
		url += "contents" + path;
		return url;
	}
}