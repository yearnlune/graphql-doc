import prompts from 'prompts';
import path from 'path';
import shell from 'shelljs';
import fs from 'fs-extra';
import logger from '@docusaurus/logger';

export default async function init(
        directory: string,
): Promise<void> {
  const docPath = await getDocPath();
  const dest = path.resolve(directory, docPath);
  const graphqlPaths = (await getGraphqlPaths()).map(v => calculateRelativePath(directory, docPath, v));
  await getGraphqlDocTemplate(dest);
  await updateSchemaPath(path.join(dest, 'docusaurus.config.ts'), graphqlPaths);
  logger.success`Created graphql doc=${dest}.`;
}

async function getDocPath(): Promise<string> {
  const {docPath} = (await prompts(
          {
            type: 'text',
            name: 'docPath',
            message: 'Where would you like to create it?',
            initial: 'doc',
            validate: (dir) => {
              if (!isRelativePath(dir)) {
                return `Invalid file path: ${dir}`;
              }
              return true;
            },
          },
          {
            onCancel() {
              process.exit(1);
            },
          },
  )) as {docPath: string};
  return docPath;
}

async function getGraphqlPaths(): Promise<string[]> {
  let loop: boolean = true;
  const graphqlPaths = new Set<string>();
  while (loop) {
    const graphqlPath = await getGraphqlPath();
    graphqlPaths.add(graphqlPath);
    loop = await isContinue();
  }
  return Array.from(graphqlPaths);
}

async function getGraphqlPath(): Promise<string> {
  const {graphqlPath} = (await prompts(
          {
            type: 'text',
            name: 'graphqlPath',
            message: 'Where is the Graphql directory? (Relative path)',
            initial: 'src/main/resources',
            validate: async (dir: string) => {
              if (!isRelativePath(dir)) {
                return `Invalid file path: ${dir}`;
              }
              return true;
            },
          },
          {
            onCancel() {
              process.exit(1);
            },
          },
  )) as {graphqlPath: string};
  return graphqlPath;
}

async function isContinue(): Promise<boolean> {
  const {isContinue} = (await prompts(
          {
            type: 'toggle',
            name: 'isContinue',
            message: 'Do you want to continue?',
            active: 'yes',
            inactive: 'no',
            initial: true,
          },
          {
            onCancel() {
              process.exit(1);
            },
          },
  )) as {isContinue: boolean};
  return isContinue;
}

async function updateSchemaPath(path: string, graphqlPaths: string[]) {
  const docusaurusConfig = (fs.readFileSync(path, 'utf-8'));
  await fs.outputFile(
          path,
          docusaurusConfig.replace(
                  `"schema" // graphql paths`,
                  graphqlPaths.map((v) => `"${v}",`).join(' '),
          ),
  );
}

async function getGraphqlDocTemplate(dest: string) {
  shell.exec(`git clone --recursive --depth 1 https://github.com/yearnlune/graphql-doc-template.git ${dest}`);
  await fs.remove(path.join(dest, '.git'));
}

function isRelativePath(path: string) {
  return RegExp('^[.\\/a-zA-Z0-9-_]+$').test(path);
}

function calculateRelativePath(root: string, source: string, target: string): string {
  let result = '';
  const rootTokens = root.split('\\');
  source.split('/').forEach((token) => {
    if (token == '..') {
      const lastToken = rootTokens.pop();
      result += lastToken + '/';
    } else if (token == '.') {
    } else {
      result = '../' + result;
    }
  });
  return result + target;
}
