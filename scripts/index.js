'use strict';

// [START functions_concepts_requests]
const fetch = require('node-fetch');
const functions = require('@google-cloud/functions-framework');

/**
 * HTTP Cloud Function that makes an HTTP request
 *
 * @param {Object} req Cloud Function request context.
 * @param {Object} res Cloud Function response context.
 */
functions.http('makeRequest', async (req, res) => {
  const url = 'https://example.com'; // URL to send the request to
  const externalRes = await fetch(url);
  res.sendStatus(externalRes.ok ? 200 : 500);
});
// [END functions_concepts_requests]