#!/usr/bin/env node

import path from 'path';
import {createRequire} from 'module';
import {program} from 'commander';

const packageJson = /** @type {import('../package.json')} */ (
  createRequire(import.meta.url)('../package.json')
);
program.version(packageJson.version);

program
  .arguments('[directory]')
  .description('Initialize GraphQL Doc')
  .action((directory) =>
    import('../lib/index.js').then(({default: init}) =>
      init(path.resolve(directory ?? '.'))
    ));

program.parse(process.argv);

if (!process.argv.slice(1).length) {
  program.outputHelp();
}

process.on('unhandledRejection', (err) => {
  console.error(err);
  process.exit(1);
});
